package pl.dabrowski.electrotools.elements.terminalelement;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;
import pl.dabrowski.electrotools.elements.terminalelement.service.create.CreateTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.read.ReadTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementDto;
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

@DisplayName("TerminalElementController Integration Tests")
class TerminalElementControllerTest extends IntegrationTest {

    static class TestCreateTerminalElementDto extends CreateTerminalElementDto {
        private final TerminalType type;

        TestCreateTerminalElementDto(double x,
                                     double y,
                                     String label,
                                     UUID parentId,
                                     UUID projectId,
                                     CreateWireDto wire,
                                     TerminalType type) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.projectId = projectId;
            this.wire = wire;
            this.type = type;
        }

        @Override
        public TerminalType getType() {
            return type;
        }
    }

    static class TestUpdateTerminalElementDto extends UpdateTerminalElementDto {
        private final TerminalType type;

        TestUpdateTerminalElementDto(double x,
                                     double y,
                                     String label,
                                     UUID parentId,
                                     UpdateWireDto wire,
                                     TerminalType type) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.wire = wire;
            this.type = type;
        }

        @Override
        public TerminalType getType() {
            return type;
        }
    }

    @Autowired
    private TerminalElementRepository terminalElementRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UUID projectId;
    private String terminalElementId;

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

        var defaultCreateDto = createTerminalDto(1.0, 2.0, "Default Terminal", null, projectId, null, TerminalType.TN_C_S);

        terminalElementId = terminalElementApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        terminalElementRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create terminal element with valid data")
        void testCreate_ValidData() {
            var createDto = createTerminalDto(5.0, 6.0, "New Terminal", null, projectId, null, TerminalType.TN_S);

            ReadTerminalElementDto created = terminalElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadTerminalElementDto.class);

            assertThat(created.getId()).isNotNull();
            assertThat(created.getX()).isEqualTo(createDto.getX());
            assertThat(created.getY()).isEqualTo(createDto.getY());
            assertThat(created.getLabel()).isEqualTo(createDto.getLabel());
            assertThat(created.getType()).isEqualTo(createDto.getType());
            assertThat(created.getElementType()).isEqualTo(BasicElementType.TERMINAL);
        }

        @Test
        @DisplayName("Should fail create when project does not exist")
        void testCreate_NonExistentProject() {
            var createDto = createTerminalDto(1.0, 1.0, "Broken", null, UUID.randomUUID(), null, TerminalType.TN_C);

            terminalElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should return 409 when terminal has parent")
        void testCreate_WithParentConflict() {
            var createDto = createTerminalDto(3.0, 4.0, "Child Terminal", UUID.fromString(terminalElementId), projectId, null, TerminalType.TN_C);

            terminalElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }

        @Test
        @DisplayName("Should return 409 when terminal has wire")
        void testCreate_WithWireConflict() {
            var wireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_15)
                    .placement(PlacementType.UNDER_PLASTER)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(33.0)
                    .build();

            var createDto = createTerminalDto(7.0, 8.0, "Terminal With Wire", null, projectId, wireDto, TerminalType.TT);

            terminalElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("update(String terminalElementId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update terminal element with valid data")
        void testUpdate_ValidData() {
            var updateDto = updateTerminalDto(10.0, 20.0, "Updated Terminal", null, null, TerminalType.TT);

            ReadTerminalElementDto updated = terminalElementApi()
                    .update(terminalElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadTerminalElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(terminalElementId);
            assertThat(updated.getX()).isEqualTo(updateDto.getX());
            assertThat(updated.getY()).isEqualTo(updateDto.getY());
            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
            assertThat(updated.getType()).isEqualTo(updateDto.getType());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreserveId() {
            var updateDto = updateTerminalDto(5.0, 5.0, "Same Id", null, null, TerminalType.TN_C);

            ReadTerminalElementDto updated = terminalElementApi()
                    .update(terminalElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadTerminalElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(terminalElementId);
        }

        @Test
        @DisplayName("Should return 409 when update sets parent")
        void testUpdate_WithParentConflict() {
            var updateDto = updateTerminalDto(2.0, 3.0, "With Parent", UUID.randomUUID(), null, TerminalType.TN_S);

            terminalElementApi()
                    .update(terminalElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }

        @Test
        @DisplayName("Should return 409 when update sets wire")
        void testUpdate_WithWireConflict() {
            var wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_25)
                    .placement(PlacementType.IN_PIPE_ON_WALL)
                    .type(WireType.MULTI_WIRE)
                    .phase(PhaseType.ONE)
                    .length(64.5)
                    .build();

            var updateDto = updateTerminalDto(2.0, 3.0, "With Wire", null, wireDto, TerminalType.TN_S);

            terminalElementApi()
                    .update(terminalElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }

        @Test
        @DisplayName("Should fail update for non-existent element")
        void testUpdate_NonExistentElement() {
            var updateDto = updateTerminalDto(1.0, 2.0, "Missing", null, null, TerminalType.TN_C_S);

            terminalElementApi()
                    .update(UUID.randomUUID().toString(), updateDto)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("deleteById(String terminalElementId)")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete existing terminal element")
        void testDeleteById_Success() {
            terminalElementApi()
                    .deleteById(terminalElementId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<ReadAbstractElementDto> trees = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(trees)
                    .extracting(ReadAbstractElementDto::getId)
                    .doesNotContain(UUID.fromString(terminalElementId));
        }

        @Test
        @DisplayName("Should handle deleting non-existent terminal element")
        void testDeleteById_NonExistentId() {
            terminalElementApi()
                    .deleteById(UUID.randomUUID().toString())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should return 400 for invalid UUID format")
        void testDeleteById_InvalidUuidFormat() {
            terminalElementApi()
                    .deleteById("not-a-uuid")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    private CreateTerminalElementDto createTerminalDto(double x,
                                                       double y,
                                                       String label,
                                                       UUID parentId,
                                                       UUID projectId,
                                                       CreateWireDto wire,
                                                       TerminalType type) {
        return new TestCreateTerminalElementDto(
                x,
                y,
                label,
                parentId,
                projectId,
                wire,
                type
        );
    }

    private UpdateTerminalElementDto updateTerminalDto(double x,
                                                       double y,
                                                       String label,
                                                       UUID parentId,
                                                       UpdateWireDto wire,
                                                       TerminalType type) {
        return new TestUpdateTerminalElementDto(
                x,
                y,
                label,
                parentId,
                wire,
                type
        );
    }
}

