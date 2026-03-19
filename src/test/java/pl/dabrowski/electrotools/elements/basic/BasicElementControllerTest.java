package pl.dabrowski.electrotools.elements.basic;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.wire.PlacementType;
import pl.dabrowski.electrotools.wire.WireDiameter;
import pl.dabrowski.electrotools.wire.WireType;
import pl.dabrowski.electrotools.wire.phase.PhaseType;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BasicElementController Integration Tests")
public class BasicElementControllerTest extends IntegrationTest {

    @Autowired
    private BasicElementRepository basicElementRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UUID projectId;
    private String elementId;
    private TestCreateDto defaultCreateDto;

    // ---------------------------------------------------------------
    // Test-specific DTOs – work around private all-args constructors
    // by subclassing and setting protected fields directly.
    // ---------------------------------------------------------------
    static class TestCreateDto extends CreateAbstractElementDto {
        TestCreateDto(double x, double y, String label, UUID projectId) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.projectId = projectId;
        }

        TestCreateDto(double x, double y, String label, UUID parentId, UUID projectId) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.projectId = projectId;
        }

        TestCreateDto(double x, double y, String label, UUID projectId, CreateWireDto wire) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.projectId = projectId;
            this.wire = wire;
        }
    }

    static class TestUpdateDto extends UpdateAbstractElementDto {
        TestUpdateDto(double x, double y, String label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

        TestUpdateDto(double x, double y, String label, UpdateWireDto wire) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.wire = wire;
        }
    }

    @BeforeEach
    void setUp() {
        projectId = UUID.fromString(
                projectApi()
                        .create(new CreateProjectDto("Test Project"))
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .path("id")
                        .toString()
        );

        defaultCreateDto = new TestCreateDto(1.0, 2.0, "Root Element", projectId);

        elementId = basicElementApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        basicElementRepository.deleteAll();
        projectRepository.deleteAll();
    }

    // ---------------------------------------------------------------

    @Nested
    @DisplayName("getTrees(UUID projectId)")
    class GetTreesTests {

        @Test
        @DisplayName("Should return elements for a project")
        void testGetTrees_Success() {
            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list when project has no elements")
        void testGetTrees_EmptyProject() {
            UUID emptyProjectId = UUID.fromString(
                    projectApi()
                            .create(new CreateProjectDto("Empty Project"))
                            .then()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .path("id")
                            .toString()
            );

            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(emptyProjectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements).isEmpty();
        }

        @Test
        @DisplayName("Should return element with correct structure")
        void testGetTrees_CorrectStructure() {
            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements).isNotEmpty();
            ReadAbstractElementDto element = elements.getFirst();
            assertThat(element.getId()).isNotNull();
            assertThat(element.getX()).isEqualTo(defaultCreateDto.getX());
            assertThat(element.getY()).isEqualTo(defaultCreateDto.getY());
            assertThat(element.getLabel()).isEqualTo(defaultCreateDto.getLabel());
            assertThat(element.getParentId()).isNull();
        }

        @Test
        @DisplayName("Should return multiple root elements each as a separate tree")
        void testGetTrees_MultipleRootsWithOwnChildren() {
            var secondRootDto = new TestCreateDto(10.0, 10.0, "Second Root", projectId);
            String secondRootId = basicElementApi()
                    .create(secondRootDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            var childOfFirstRootDto = new TestCreateDto(2.0, 3.0, "Child of First Root", UUID.fromString(elementId), projectId);
            basicElementApi()
                    .create(childOfFirstRootDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            var childOfSecondRootDto = new TestCreateDto(11.0, 12.0, "Child of Second Root", UUID.fromString(secondRootId), projectId);
            basicElementApi()
                    .create(childOfSecondRootDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            List<ReadAbstractElementDto> trees = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(trees).hasSize(2);

            ReadAbstractElementDto firstRoot = trees.stream()
                    .filter(e -> e.getId().toString().equals(elementId))
                    .findFirst()
                    .orElseThrow();

            ReadAbstractElementDto secondRoot = trees.stream()
                    .filter(e -> e.getId().toString().equals(secondRootId))
                    .findFirst()
                    .orElseThrow();

            assertThat(firstRoot.getChildren()).hasSize(1);
            assertThat(firstRoot.getChildren().getFirst().getLabel()).isEqualTo(childOfFirstRootDto.getLabel());

            assertThat(secondRoot.getChildren()).hasSize(1);
            assertThat(secondRoot.getChildren().getFirst().getLabel()).isEqualTo(childOfSecondRootDto.getLabel());
        }

        @Test
        @DisplayName("Should return child element nested under its parent")
        void testGetTrees_WithChildren() {
            var childDto = new TestCreateDto(3.0, 4.0, "Child Element", UUID.fromString(elementId), projectId);
            basicElementApi()
                    .create(childDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            List<ReadAbstractElementDto> trees = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(trees).hasSize(1);
            assertThat(trees.getFirst().getChildren()).hasSize(1);
            assertThat(trees.getFirst().getChildren().getFirst().getLabel()).isEqualTo(childDto.getLabel());
        }
    }

    // ---------------------------------------------------------------

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create element with valid data")
        void testCreate_ValidData() {
            var createDto = new TestCreateDto(5.0, 6.0, "New Element", projectId);
            ReadAbstractElementDto element = basicElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(element.getId()).isNotNull();
            assertThat(element.getX()).isEqualTo(createDto.getX());
            assertThat(element.getY()).isEqualTo(createDto.getY());
            assertThat(element.getLabel()).isEqualTo(createDto.getLabel());
        }

        @Test
        @DisplayName("Should create child element with parent reference")
        void testCreate_WithParent() {
            var childDto = new TestCreateDto(3.0, 4.0, "Child Element", UUID.fromString(elementId), projectId);
            ReadAbstractElementDto child = basicElementApi()
                    .create(childDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(child.getId()).isNotNull();
            assertThat(child.getParentId()).isEqualTo(childDto.getParentId());
        }

        @Test
        @DisplayName("Should create element at zero coordinates")
        void testCreate_ZeroCoordinates() {
            var createDto = new TestCreateDto(0.0, 0.0, "Zero Position", projectId);
            ReadAbstractElementDto element = basicElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(element.getX()).isEqualTo(createDto.getX());
            assertThat(element.getY()).isEqualTo(createDto.getY());
        }

        @Test
        @DisplayName("Should create element without a label")
        void testCreate_NullLabel() {
            var createDto = new TestCreateDto(1.0, 1.0, null, projectId);
            ReadAbstractElementDto element = basicElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(element.getId()).isNotNull();
            assertThat(element.getLabel()).isEqualTo(createDto.getLabel());
        }
    }

    // ---------------------------------------------------------------

    @Nested
    @DisplayName("update(String basicElementId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update element with valid data")
        void testUpdate_ValidData() {
            var updateDto = new TestUpdateDto(10.0, 20.0, "Updated Label");
            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(elementId);
            assertThat(updated.getX()).isEqualTo(updateDto.getX());
            assertThat(updated.getY()).isEqualTo(updateDto.getY());
            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent element")
        void testUpdate_NonExistentElement() {
            var updateDto = new TestUpdateDto(1.0, 2.0, "Label");
            basicElementApi()
                    .update(UUID.randomUUID().toString(), updateDto)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreservesId() {
            var updateDto = new TestUpdateDto(5.0, 5.0, "Same Id");
            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(elementId);
        }

        @Test
        @DisplayName("Should update label to null")
        void testUpdate_NullLabel() {
            var updateDto = new TestUpdateDto(1.0, 2.0, null);
            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
        }

        @Test
        @DisplayName("Should add wire to element during update")
        void testUpdate_AddWire() {
            var wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_15)
                    .placement(PlacementType.IN_PIPE_ON_WALL)
                    .type(WireType.MULTI_WIRE)
                    .phase(PhaseType.ONE)
                    .length(100.0)
                    .build();

            var updateDto = new TestUpdateDto(5.0, 6.0, "Updated with Wire", wireDto);

            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(elementId);
            assertThat(updated.getWire()).isNotNull();
            assertThat(updated.getWire().diameter()).isEqualTo(wireDto.getDiameter());
            assertThat(updated.getWire().placement()).isEqualTo(wireDto.getPlacement());
            assertThat(updated.getWire().type()).isEqualTo(wireDto.getType());
            assertThat(updated.getWire().phase()).isEqualTo(wireDto.getPhase());
            assertThat(updated.getWire().length()).isEqualTo(wireDto.getLength());
        }

        @Test
        @DisplayName("Should update existing wire on element")
        void testUpdate_ChangeWire() {
            var initialWireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_05)
                    .placement(PlacementType.DIRECT_ON_WALL)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(30.0)
                    .build();

            var createDto = new TestCreateDto(2.0, 3.0, "Element to Update Wire", projectId, initialWireDto);

            String elementIdWithWire = basicElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            var newWireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_25)
                    .placement(PlacementType.IN_PIPE_ON_WALL)
                    .type(WireType.MULTI_WIRE)
                    .phase(PhaseType.ONE)
                    .length(75.5)
                    .build();

            var updateDto = new TestUpdateDto(2.0, 3.0, "Element to Update Wire", newWireDto);

            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementIdWithWire, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getWire().diameter()).isEqualTo(newWireDto.getDiameter());
            assertThat(updated.getWire().placement()).isEqualTo(newWireDto.getPlacement());
            assertThat(updated.getWire().type()).isEqualTo(newWireDto.getType());
            assertThat(updated.getWire().phase()).isEqualTo(newWireDto.getPhase());
            assertThat(updated.getWire().length()).isEqualTo(newWireDto.getLength());
        }

        @Test
        @DisplayName("Should remove wire from element during update")
        void testUpdate_RemoveWire() {
            var wireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_40)
                    .placement(PlacementType.UNDER_PLASTER)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(60.0)
                    .build();

            var createDto = new TestCreateDto(3.0, 4.0, "Element with Wire to Remove", projectId, wireDto);

            String elementIdWithWire = basicElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            var updateDtoWithoutWire = new TestUpdateDto(3.0, 4.0, "Wire Removed");

            ReadAbstractElementDto updated = basicElementApi()
                    .update(elementIdWithWire, updateDtoWithoutWire)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadAbstractElementDto.class);

            assertThat(updated.getWire()).isNull();
        }
    }

    // ---------------------------------------------------------------

    @Nested
    @DisplayName("updatePositions(List<UpdateBasicElementPositionDto> changes)")
    class UpdatePositionsTests {

        @Test
        @DisplayName("Should update position of single element and return 200")
        void testUpdatePositions_SingleElement() {
            var change = new UpdateBasicElementPositionDto(UUID.fromString(elementId), 99.0, 88.0);

            basicElementApi()
                    .updatePositions(List.of(change))
                    .then()
                    .statusCode(HttpStatus.OK.value());

            ReadAbstractElementDto updated = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<List<ReadAbstractElementDto>>() {
                    })
                    .stream()
                    .filter(e -> e.getId().toString().equals(elementId))
                    .findFirst()
                    .orElseThrow();

            assertThat(updated.getX()).isEqualTo(change.x());
            assertThat(updated.getY()).isEqualTo(change.y());
        }

        @Test
        @DisplayName("Should update positions of multiple elements")
        void testUpdatePositions_MultipleElements() {
            String secondElementId = basicElementApi()
                    .create(new TestCreateDto(0.0, 0.0, "Second Element", projectId))
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            var change1 = new UpdateBasicElementPositionDto(UUID.fromString(elementId), 10.0, 20.0);

            var change2 = new UpdateBasicElementPositionDto(UUID.fromString(secondElementId), 30.0, 40.0);

            basicElementApi()
                    .updatePositions(List.of(change1, change2))
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<ReadAbstractElementDto> trees = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            ReadAbstractElementDto updatedFirst = trees.stream()
                    .filter(e -> e.getId().toString().equals(elementId))
                    .findFirst()
                    .orElseThrow();

            ReadAbstractElementDto updatedSecond = trees.stream()
                    .filter(e -> e.getId().toString().equals(secondElementId))
                    .findFirst()
                    .orElseThrow();

            assertThat(updatedFirst.getX()).isEqualTo(change1.x());
            assertThat(updatedFirst.getY()).isEqualTo(change1.y());

            assertThat(updatedSecond.getX()).isEqualTo(change2.x());
            assertThat(updatedSecond.getY()).isEqualTo(change2.y());
        }

        @Test
        @DisplayName("Should handle empty list and return 200")
        void testUpdatePositions_EmptyList() {
            basicElementApi()
                    .updatePositions(List.of())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    // ---------------------------------------------------------------

    @Nested
    @DisplayName("remove(String... ids)")
    class RemoveTests {

        @Test
        @DisplayName("Should remove single element by ID")
        void testRemove_SingleElement() {
            var toRemoveDto = new TestCreateDto(7.0, 8.0, "Element to Remove", projectId);
            String idToRemove = basicElementApi()
                    .create(toRemoveDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            basicElementApi()
                    .remove(idToRemove)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements.stream().noneMatch(e -> e.getId().toString().equals(idToRemove))).isTrue();
        }

        @Test
        @DisplayName("Should remove multiple elements")
        void testRemove_MultipleElements() {
            var dto1 = new TestCreateDto(1.0, 1.0, "Element 1", projectId);
            String id1 = basicElementApi()
                    .create(dto1)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            var dto2 = new TestCreateDto(2.0, 2.0, "Element 2", projectId);
            String id2 = basicElementApi()
                    .create(dto2)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            basicElementApi()
                    .remove(id1, id2)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements.stream().noneMatch(e -> e.getId().toString().equals(id1))).isTrue();
            assertThat(elements.stream().noneMatch(e -> e.getId().toString().equals(id2))).isTrue();
        }

        @Test
        @DisplayName("Should handle empty list and return 200")
        void testRemove_EmptyList() {
            basicElementApi()
                    .remove()
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should handle non-existent IDs and return 200")
        void testRemove_NonExistentIds() {
            basicElementApi()
                    .remove(UUID.randomUUID().toString(), UUID.randomUUID().toString())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should remove element without affecting other elements")
        void testRemove_PreservesOtherElements() {
            var elementToKeepDto = new TestCreateDto(5.0, 5.0, "Element to Keep", projectId);
            String elementToKeepId = basicElementApi()
                    .create(elementToKeepDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            basicElementApi()
                    .remove(elementId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<ReadAbstractElementDto> elements = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(elements.stream().anyMatch(e -> e.getId().toString().equals(elementToKeepId))).isTrue();
            assertThat(elements.stream().noneMatch(e -> e.getId().toString().equals(elementId))).isTrue();
        }
    }
}

