package pl.dabrowski.electrotools.elements.load;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
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

@DisplayName("LoadElementController Integration Tests")
class LoadElementControllerTest extends IntegrationTest {

    static class TestCreateLoadElementDto extends CreateLoadElementDto {
        TestCreateLoadElementDto(double x,
                                 double y,
                                 String label,
                                 UUID parentId,
                                 UUID projectId,
                                 double drawPower,
                                 double powerFactor,
                                 boolean highStartCurrent,
                                 Config config,
                                 boolean zeroed,
                                 CreateWireDto wire) {
            super(drawPower, powerFactor, highStartCurrent, config, zeroed);
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.projectId = projectId;
            this.wire = wire;
        }
    }

    static class TestUpdateLoadElementDto extends UpdateLoadElementDto {
        TestUpdateLoadElementDto(double x,
                                 double y,
                                 String label,
                                 UUID parentId,
                                 double drawPower,
                                 double powerFactor,
                                 boolean highStartCurrent,
                                 Config config,
                                 boolean zeroed,
                                 UpdateWireDto wire) {
            super(drawPower, powerFactor, highStartCurrent, config, zeroed);
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.wire = wire;
        }
    }

    @Autowired
    private LoadElementRepository loadElementRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UUID projectId;
    private String loadElementId;
    private CreateLoadElementDto defaultCreateDto;

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

        defaultCreateDto = createLoadDto(1.0, 2.0, "Default Load", null, projectId,
                1500.0, 0.9, true, Config.STAR, false, null);

        loadElementId = loadElementApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        loadElementRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create load element with valid data")
        void testCreate_ValidData() {
            CreateLoadElementDto createDto = createLoadDto(5.0, 6.0, "New Load", null, projectId,
                    2200.0, 0.85, true, Config.DELTA, true, null);

            ReadLoadElementDto created = loadElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

            assertThat(created.getId()).isNotNull();
            assertThat(created.getX()).isEqualTo(createDto.getX());
            assertThat(created.getY()).isEqualTo(createDto.getY());
            assertThat(created.getLabel()).isEqualTo(createDto.getLabel());
            assertThat(created.getDrawPower()).isEqualTo(createDto.getDrawPower());
            assertThat(created.getPowerFactor()).isEqualTo(createDto.getPowerFactor());
            assertThat(created.getHighStartCurrent()).isEqualTo(createDto.isHighStartCurrent());
            assertThat(created.getConfig()).isEqualTo(createDto.getConfig());
            assertThat(created.getZeroed()).isEqualTo(createDto.isZeroed());
            assertThat(created.getElementType()).isEqualTo(BasicElementType.LOAD);
        }

        @Test
        @DisplayName("Should create child load element with parent reference")
        void testCreate_WithParent() {
            CreateLoadElementDto childDto = createLoadDto(3.0, 4.0, "Child Load", UUID.fromString(loadElementId), projectId,
                    900.0, 0.95, false, Config.STAR, false, null);

            ReadLoadElementDto created = loadElementApi()
                    .create(childDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

            assertThat(created.getParentId()).isEqualTo(childDto.getParentId());
        }

        @Test
        @DisplayName("Should create load element with wire")
        void testCreate_WithWire() {
            CreateWireDto wireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_15)
                    .placement(PlacementType.UNDER_PLASTER)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(33.0)
                    .build();

            CreateLoadElementDto createDto = createLoadDto(7.0, 8.0, "Load With Wire", null, projectId,
                    1800.0, 0.8, true, Config.STAR, false, wireDto);

            ReadLoadElementDto created = loadElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

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
            CreateLoadElementDto createDto = createLoadDto(1.0, 1.0, "Broken", null, UUID.randomUUID(),
                    1000.0, 1.0, false, Config.STAR, false, null);

            loadElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    @DisplayName("update(String loadElementId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update load element with valid data")
        void testUpdate_ValidData() {
            UpdateLoadElementDto updateDto = updateLoadDto(10.0, 20.0, "Updated Load", null,
                    3000.0, 0.7, true, Config.DELTA, true, null);

            ReadLoadElementDto updated = loadElementApi()
                    .update(loadElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(loadElementId);
            assertThat(updated.getX()).isEqualTo(updateDto.getX());
            assertThat(updated.getY()).isEqualTo(updateDto.getY());
            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
            assertThat(updated.getDrawPower()).isEqualTo(updateDto.getDrawPower());
            assertThat(updated.getPowerFactor()).isEqualTo(updateDto.getPowerFactor());
            assertThat(updated.getHighStartCurrent()).isEqualTo(updateDto.isHighStartCurrent());
            assertThat(updated.getConfig()).isEqualTo(updateDto.getConfig());
            assertThat(updated.getZeroed()).isEqualTo(updateDto.isZeroed());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreserveId() {
            UpdateLoadElementDto updateDto = updateLoadDto(5.0, 5.0, "Same Id", null,
                    1600.0, 0.93, false, Config.STAR, false, null);

            ReadLoadElementDto updated = loadElementApi()
                    .update(loadElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(loadElementId);
        }

        @Test
        @DisplayName("Should add wire during update")
        void testUpdate_AddWire() {
            UpdateWireDto wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_25)
                    .placement(PlacementType.IN_PIPE_ON_WALL)
                    .type(WireType.MULTI_WIRE)
                    .phase(PhaseType.ONE)
                    .length(64.5)
                    .build();

            UpdateLoadElementDto updateDto = updateLoadDto(2.0, 3.0, "With Wire", null,
                    1234.0, 0.99, false, Config.STAR, false, wireDto);

            ReadLoadElementDto updated = loadElementApi()
                    .update(loadElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

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
            UpdateWireDto wireDto = UpdateWireDto.builder()
                    .diameter(WireDiameter.D_1)
                    .placement(PlacementType.DIRECT_ON_WALL)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(12.0)
                    .build();

            UpdateLoadElementDto setupWireDto = updateLoadDto(1.0, 2.0, "Wire Setup", null,
                    1500.0, 0.9, true, Config.STAR, false, wireDto);

            loadElementApi().update(loadElementId, setupWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            UpdateLoadElementDto removeWireDto = updateLoadDto(setupWireDto.getX(), setupWireDto.getY(),
                    "No Wire", setupWireDto.getParentId(), setupWireDto.getDrawPower(), setupWireDto.getPowerFactor(),
                    setupWireDto.isHighStartCurrent(), setupWireDto.getConfig(), setupWireDto.isZeroed(), null);

            ReadLoadElementDto updated = loadElementApi()
                    .update(loadElementId, removeWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadLoadElementDto.class);

            assertThat(updated.getWire()).isNull();
        }

        @Test
        @DisplayName("Should fail update for non-existent element")
        void testUpdate_NonExistentElement() {
            UpdateLoadElementDto updateDto = updateLoadDto(1.0, 2.0, "Missing", null,
                    1100.0, 0.95, false, Config.STAR, false, null);

            loadElementApi()
                    .update(UUID.randomUUID().toString(), updateDto)
                    .then()
                    .statusCode(404);
        }
    }

    @Nested
    @DisplayName("deleteById(String loadElementId)")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete existing load element")
        void testDeleteById_Success() {
            loadElementApi()
                    .deleteById(loadElementId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            List<String> treeIds = basicElementApi()
                    .getTrees(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .path("id");

            assertThat(treeIds).doesNotContain(loadElementId);
        }

        @Test
        @DisplayName("Should handle deleting non-existent load element")
        void testDeleteById_NonExistentId() {
            loadElementApi()
                    .deleteById(UUID.randomUUID().toString())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should return 400 for invalid UUID format")
        void testDeleteById_InvalidUuidFormat() {
            loadElementApi()
                    .deleteById("not-a-uuid")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    private CreateLoadElementDto createLoadDto(double x,
                                               double y,
                                               String label,
                                               UUID parentId,
                                               UUID projectId,
                                               double drawPower,
                                               double powerFactor,
                                               boolean highStartCurrent,
                                               Config config,
                                               boolean zeroed,
                                               CreateWireDto wire) {
        return new TestCreateLoadElementDto(
                x,
                y,
                label,
                parentId,
                projectId,
                drawPower,
                powerFactor,
                highStartCurrent,
                config,
                zeroed,
                wire
        );
    }

    private UpdateLoadElementDto updateLoadDto(double x,
                                               double y,
                                               String label,
                                               UUID parentId,
                                               double drawPower,
                                               double powerFactor,
                                               boolean highStartCurrent,
                                               Config config,
                                               boolean zeroed,
                                               UpdateWireDto wire) {
        return new TestUpdateLoadElementDto(
                x,
                y,
                label,
                parentId,
                drawPower,
                powerFactor,
                highStartCurrent,
                config,
                zeroed,
                wire
        );
    }
}