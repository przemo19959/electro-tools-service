package pl.dabrowski.electrotools.elements.rcdelement;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;
import pl.dabrowski.electrotools.elements.rcdelement.service.create.CreateRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.read.ReadRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementDto;
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

@DisplayName("RcdElementController Integration Tests")
class RcdElementControllerTest extends IntegrationTest {

    static class TestCreateRcdElementDto extends CreateRcdElementDto {
        private final int nominalCurrent;
        private final int diffCurrent;
        private final int poleNumber;

        TestCreateRcdElementDto(double x,
                                double y,
                                String label,
                                UUID parentId,
                                UUID projectId,
                                CreateWireDto wire,
                                int nominalCurrent,
                                int diffCurrent,
                                int poleNumber) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.projectId = projectId;
            this.wire = wire;
            this.nominalCurrent = nominalCurrent;
            this.diffCurrent = diffCurrent;
            this.poleNumber = poleNumber;
        }

        @Override
        public int getNominalCurrent() {
            return nominalCurrent;
        }

        @Override
        public int getDiffCurrent() {
            return diffCurrent;
        }

        @Override
        public int getPoleNumber() {
            return poleNumber;
        }
    }

    static class TestUpdateRcdElementDto extends UpdateRcdElementDto {
        private final int nominalCurrent;
        private final int diffCurrent;
        private final int poleNumber;

        TestUpdateRcdElementDto(double x,
                                double y,
                                String label,
                                UUID parentId,
                                UpdateWireDto wire,
                                int nominalCurrent,
                                int diffCurrent,
                                int poleNumber) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.wire = wire;
            this.nominalCurrent = nominalCurrent;
            this.diffCurrent = diffCurrent;
            this.poleNumber = poleNumber;
        }

        @Override
        public int getNominalCurrent() {
            return nominalCurrent;
        }

        @Override
        public int getDiffCurrent() {
            return diffCurrent;
        }

        @Override
        public int getPoleNumber() {
            return poleNumber;
        }
    }

    @Autowired
    private RcdElementRepository rcdElementRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UUID projectId;
    private String rcdElementId;

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

        var defaultCreateDto = createRcdDto(1.0, 2.0, "Default RCD", null, projectId, null, 40, 30, 2);

        rcdElementId = rcdElementApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        rcdElementRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create RCD element with valid data")
        void testCreate_ValidData() {
            var createDto = createRcdDto(5.0, 6.0, "New RCD", null, projectId, null, 25, 30, 4);

            ReadRcdElementDto created = rcdElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(created.getId()).isNotNull();
            assertThat(created.getX()).isEqualTo(createDto.getX());
            assertThat(created.getY()).isEqualTo(createDto.getY());
            assertThat(created.getLabel()).isEqualTo(createDto.getLabel());
            assertThat(created.getNominalCurrent()).isEqualTo(createDto.getNominalCurrent());
            assertThat(created.getDiffCurrent()).isEqualTo(createDto.getDiffCurrent());
            assertThat(created.getPoleNumber()).isEqualTo(createDto.getPoleNumber());
            assertThat(created.getElementType()).isEqualTo(BasicElementType.RCD);
        }

        @Test
        @DisplayName("Should create child RCD element with parent reference")
        void testCreate_WithParent() {
            var childDto = createRcdDto(3.0, 4.0, "Child RCD", UUID.fromString(rcdElementId), projectId, null, 16, 30, 2);

            ReadRcdElementDto created = rcdElementApi()
                    .create(childDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(created.getParentId()).isEqualTo(childDto.getParentId());
        }

        @Test
        @DisplayName("Should create RCD element with wire")
        void testCreate_WithWire() {
            var wireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_15)
                    .placement(PlacementType.UNDER_PLASTER)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(33.0)
                    .build();

            var createDto = createRcdDto(7.0, 8.0, "RCD With Wire", null, projectId, wireDto, 63, 30, 2);

            ReadRcdElementDto created = rcdElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(created.getWire()).isNotNull();
            assertThat(created.getWire().diameter()).isEqualTo(wireDto.getDiameter());
            assertThat(created.getWire().placement()).isEqualTo(wireDto.getPlacement());
            assertThat(created.getWire().type()).isEqualTo(wireDto.getType());
            assertThat(created.getWire().phase()).isEqualTo(wireDto.getPhase());
            assertThat(created.getWire().length()).isEqualTo(wireDto.getLength());
        }

        @Test
        @DisplayName("Should fail create when project does not exist")
        void testCreate_NonExistentProject() {
            var createDto = createRcdDto(1.0, 1.0, "Broken", null, UUID.randomUUID(), null, 40, 30, 2);

            rcdElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should return 409 for illegal pole number")
        void testCreate_IllegalPoleNumber() {
            var createDto = createRcdDto(1.0, 1.0, "Illegal Pole", null, projectId, null, 40, 30, 3);

            rcdElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("update(String rcdElementId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update RCD element with valid data")
        void testUpdate_ValidData() {
            var updateDto = updateRcdDto(10.0, 20.0, "Updated RCD", null, null, 63, 100, 4);

            ReadRcdElementDto updated = rcdElementApi()
                    .update(rcdElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(rcdElementId);
            assertThat(updated.getX()).isEqualTo(updateDto.getX());
            assertThat(updated.getY()).isEqualTo(updateDto.getY());
            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
            assertThat(updated.getNominalCurrent()).isEqualTo(updateDto.getNominalCurrent());
            assertThat(updated.getDiffCurrent()).isEqualTo(updateDto.getDiffCurrent());
            assertThat(updated.getPoleNumber()).isEqualTo(updateDto.getPoleNumber());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreserveId() {
            var updateDto = updateRcdDto(5.0, 5.0, "Same Id", null, null, 25, 30, 2);

            ReadRcdElementDto updated = rcdElementApi()
                    .update(rcdElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(rcdElementId);
        }

        @Test
        @DisplayName("Should add wire during update")
        void testUpdate_AddWire() {
            var wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_25)
                    .placement(PlacementType.IN_PIPE_ON_WALL)
                    .type(WireType.MULTI_WIRE)
                    .phase(PhaseType.ONE)
                    .length(64.5)
                    .build();

            var updateDto = updateRcdDto(2.0, 3.0, "With Wire", null, wireDto, 40, 30, 2);

            ReadRcdElementDto updated = rcdElementApi()
                    .update(rcdElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(updated.getWire()).isNotNull();
            assertThat(updated.getWire().diameter()).isEqualTo(wireDto.getDiameter());
            assertThat(updated.getWire().placement()).isEqualTo(wireDto.getPlacement());
            assertThat(updated.getWire().type()).isEqualTo(wireDto.getType());
            assertThat(updated.getWire().phase()).isEqualTo(wireDto.getPhase());
            assertThat(updated.getWire().length()).isEqualTo(wireDto.getLength());
        }

        @Test
        @DisplayName("Should remove wire during update")
        void testUpdate_RemoveWire() {
            var wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_1)
                    .placement(PlacementType.DIRECT_ON_WALL)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(12.0)
                    .build();

            var setupWireDto = updateRcdDto(1.0, 2.0, "Wire Setup", null, wireDto, 16, 30, 2);

            rcdElementApi()
                    .update(rcdElementId, setupWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            var removeWireDto = updateRcdDto(
                    setupWireDto.getX(),
                    setupWireDto.getY(),
                    "No Wire",
                    setupWireDto.getParentId(),
                    null,
                    setupWireDto.getNominalCurrent(),
                    setupWireDto.getDiffCurrent(),
                    setupWireDto.getPoleNumber()
            );

            ReadRcdElementDto updated = rcdElementApi()
                    .update(rcdElementId, removeWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadRcdElementDto.class);

            assertThat(updated.getWire()).isNull();
        }

        @Test
        @DisplayName("Should fail update for non-existent element")
        void testUpdate_NonExistentElement() {
            var updateDto = updateRcdDto(1.0, 2.0, "Missing", null, null, 25, 30, 2);

            rcdElementApi()
                    .update(UUID.randomUUID().toString(), updateDto)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should return 409 for illegal pole number during update")
        void testUpdate_IllegalPoleNumber() {
            var updateDto = updateRcdDto(1.0, 2.0, "Illegal Pole", null, null, 25, 30, 1);

            rcdElementApi()
                    .update(rcdElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("deleteById(String rcdElementId)")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete existing RCD element")
        void testDeleteById_Success() {
            rcdElementApi()
                    .deleteById(rcdElementId)
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
                    .doesNotContain(UUID.fromString(rcdElementId));
        }

        @Test
        @DisplayName("Should handle deleting non-existent RCD element")
        void testDeleteById_NonExistentId() {
            rcdElementApi()
                    .deleteById(UUID.randomUUID().toString())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should return 400 for invalid UUID format")
        void testDeleteById_InvalidUuidFormat() {
            rcdElementApi()
                    .deleteById("not-a-uuid")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    private CreateRcdElementDto createRcdDto(double x,
                                             double y,
                                             String label,
                                             UUID parentId,
                                             UUID projectId,
                                             CreateWireDto wire,
                                             int nominalCurrent,
                                             int diffCurrent,
                                             int poleNumber) {
        return new TestCreateRcdElementDto(
                x,
                y,
                label,
                parentId,
                projectId,
                wire,
                nominalCurrent,
                diffCurrent,
                poleNumber
        );
    }

    private UpdateRcdElementDto updateRcdDto(double x,
                                             double y,
                                             String label,
                                             UUID parentId,
                                             UpdateWireDto wire,
                                             int nominalCurrent,
                                             int diffCurrent,
                                             int poleNumber) {
        return new TestUpdateRcdElementDto(
                x,
                y,
                label,
                parentId,
                wire,
                nominalCurrent,
                diffCurrent,
                poleNumber
        );
    }
}

