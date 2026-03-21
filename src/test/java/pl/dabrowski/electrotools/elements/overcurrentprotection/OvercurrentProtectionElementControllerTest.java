package pl.dabrowski.electrotools.elements.overcurrentprotection;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;
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

@DisplayName("OvercurrentProtectionElementController Integration Tests")
class OvercurrentProtectionElementControllerTest extends IntegrationTest {

    static class TestCreateOvercurrentProtectionElementDto extends CreateOvercurrentProtectionElementDto {
        private final OvercurrentProtectionType type;
        private final int amperage;

        TestCreateOvercurrentProtectionElementDto(double x,
                                                  double y,
                                                  String label,
                                                  UUID parentId,
                                                  UUID projectId,
                                                  CreateWireDto wire,
                                                  OvercurrentProtectionType type,
                                                  int amperage) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.projectId = projectId;
            this.wire = wire;
            this.type = type;
            this.amperage = amperage;
        }

        @Override
        public OvercurrentProtectionType getType() {
            return type;
        }

        @Override
        public int getAmperage() {
            return amperage;
        }
    }

    static class TestUpdateOvercurrentProtectionElementDto extends UpdateOvercurrentProtectionElementDto {
        private final OvercurrentProtectionType type;
        private final int amperage;

        TestUpdateOvercurrentProtectionElementDto(double x,
                                                  double y,
                                                  String label,
                                                  UUID parentId,
                                                  UpdateWireDto wire,
                                                  OvercurrentProtectionType type,
                                                  int amperage) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.parentId = parentId;
            this.wire = wire;
            this.type = type;
            this.amperage = amperage;
        }

        @Override
        public OvercurrentProtectionType getType() {
            return type;
        }

        @Override
        public int getAmperage() {
            return amperage;
        }
    }

    @Autowired
    private OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UUID projectId;
    private String overcurrentProtectionElementId;
    private CreateOvercurrentProtectionElementDto defaultCreateDto;

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

        defaultCreateDto = createOvercurrentDto(
                1.0,
                2.0,
                "Default Overcurrent",
                null,
                projectId,
                null,
                OvercurrentProtectionType.B,
                16
        );

        overcurrentProtectionElementId = overcurrentProtectionElementApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        overcurrentProtectionElementRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create overcurrent protection element with valid data")
        void testCreate_ValidData() {
            CreateOvercurrentProtectionElementDto createDto = createOvercurrentDto(
                    5.0,
                    6.0,
                    "New Overcurrent",
                    null,
                    projectId,
                    null,
                    OvercurrentProtectionType.C,
                    25
            );

            ReadOvercurrentProtectionElementDto created = overcurrentProtectionElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

            assertThat(created.getId()).isNotNull();
            assertThat(created.getX()).isEqualTo(createDto.getX());
            assertThat(created.getY()).isEqualTo(createDto.getY());
            assertThat(created.getLabel()).isEqualTo(createDto.getLabel());
            assertThat(created.getType()).isEqualTo(createDto.getType());
            assertThat(created.getAmperage()).isEqualTo(createDto.getAmperage());
            assertThat(created.getElementType()).isEqualTo(BasicElementType.OVER_CURRENT_PROTECTION);
        }

        @Test
        @DisplayName("Should create child overcurrent protection element with parent reference")
        void testCreate_WithParent() {
            CreateOvercurrentProtectionElementDto childDto = createOvercurrentDto(
                    3.0,
                    4.0,
                    "Child Overcurrent",
                    UUID.fromString(overcurrentProtectionElementId),
                    projectId,
                    null,
                    OvercurrentProtectionType.A,
                    10
            );

            ReadOvercurrentProtectionElementDto created = overcurrentProtectionElementApi()
                    .create(childDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

            assertThat(created.getParentId()).isEqualTo(childDto.getParentId());
        }

        @Test
        @DisplayName("Should create overcurrent protection element with wire")
        void testCreate_WithWire() {
            CreateWireDto wireDto = CreateWireDto.builder()
                    .diameter(WireDiameter.D_15)
                    .placement(PlacementType.UNDER_PLASTER)
                    .type(WireType.ONE_WIRE)
                    .phase(PhaseType.THREE)
                    .length(33.0)
                    .build();

            CreateOvercurrentProtectionElementDto createDto = createOvercurrentDto(
                    7.0,
                    8.0,
                    "Overcurrent With Wire",
                    null,
                    projectId,
                    wireDto,
                    OvercurrentProtectionType.D,
                    32
            );

            ReadOvercurrentProtectionElementDto created = overcurrentProtectionElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

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
            CreateOvercurrentProtectionElementDto createDto = createOvercurrentDto(
                    1.0,
                    1.0,
                    "Broken",
                    null,
                    UUID.randomUUID(),
                    null,
                    OvercurrentProtectionType.B,
                    20
            );

            overcurrentProtectionElementApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("update(String overcurrentProtectionElementId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update overcurrent protection element with valid data")
        void testUpdate_ValidData() {
            UpdateOvercurrentProtectionElementDto updateDto = updateOvercurrentDto(
                    10.0,
                    20.0,
                    "Updated Overcurrent",
                    null,
                    null,
                    OvercurrentProtectionType.A,
                    40
            );

            ReadOvercurrentProtectionElementDto updated = overcurrentProtectionElementApi()
                    .update(overcurrentProtectionElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(overcurrentProtectionElementId);
            assertThat(updated.getX()).isEqualTo(updateDto.getX());
            assertThat(updated.getY()).isEqualTo(updateDto.getY());
            assertThat(updated.getLabel()).isEqualTo(updateDto.getLabel());
            assertThat(updated.getParentId()).isNull();
            assertThat(updated.getWire()).isNull();
            assertThat(updated.getType()).isEqualTo(updateDto.getType());
            assertThat(updated.getAmperage()).isEqualTo(updateDto.getAmperage());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreserveId() {
            UpdateOvercurrentProtectionElementDto updateDto = updateOvercurrentDto(
                    5.0,
                    5.0,
                    "Same Id",
                    null,
                    null,
                    OvercurrentProtectionType.C,
                    50
            );

            ReadOvercurrentProtectionElementDto updated = overcurrentProtectionElementApi()
                    .update(overcurrentProtectionElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

            assertThat(updated.getId().toString()).isEqualTo(overcurrentProtectionElementId);
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

            UpdateOvercurrentProtectionElementDto updateDto = updateOvercurrentDto(
                    2.0,
                    3.0,
                    "With Wire",
                    null,
                    wireDto,
                    OvercurrentProtectionType.B,
                    20
            );

            ReadOvercurrentProtectionElementDto updated = overcurrentProtectionElementApi()
                    .update(overcurrentProtectionElementId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

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

            UpdateOvercurrentProtectionElementDto setupWireDto = updateOvercurrentDto(
                    1.0,
                    2.0,
                    "Wire Setup",
                    null,
                    wireDto,
                    OvercurrentProtectionType.C,
                    16
            );

            overcurrentProtectionElementApi()
                    .update(overcurrentProtectionElementId, setupWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            UpdateOvercurrentProtectionElementDto removeWireDto = updateOvercurrentDto(
                    setupWireDto.getX(),
                    setupWireDto.getY(),
                    "No Wire",
                    setupWireDto.getParentId(),
                    null,
                    setupWireDto.getType(),
                    setupWireDto.getAmperage()
            );

            ReadOvercurrentProtectionElementDto updated = overcurrentProtectionElementApi()
                    .update(overcurrentProtectionElementId, removeWireDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadOvercurrentProtectionElementDto.class);

            assertThat(updated.getWire()).isNull();
        }

        @Test
        @DisplayName("Should fail update for non-existent element")
        void testUpdate_NonExistentElement() {
            UpdateOvercurrentProtectionElementDto updateDto = updateOvercurrentDto(
                    1.0,
                    2.0,
                    "Missing",
                    null,
                    null,
                    OvercurrentProtectionType.D,
                    63
            );

            overcurrentProtectionElementApi()
                    .update(UUID.randomUUID().toString(), updateDto)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("deleteById(String overcurrentProtectionElementId)")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete existing overcurrent protection element")
        void testDeleteById_Success() {
            overcurrentProtectionElementApi()
                    .deleteById(overcurrentProtectionElementId)
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
                    .doesNotContain(UUID.fromString(overcurrentProtectionElementId));
        }

        @Test
        @DisplayName("Should handle deleting non-existent overcurrent protection element")
        void testDeleteById_NonExistentId() {
            overcurrentProtectionElementApi()
                    .deleteById(UUID.randomUUID().toString())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should return 400 for invalid UUID format")
        void testDeleteById_InvalidUuidFormat() {
            overcurrentProtectionElementApi()
                    .deleteById("not-a-uuid")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    private CreateOvercurrentProtectionElementDto createOvercurrentDto(double x,
                                                                       double y,
                                                                       String label,
                                                                       UUID parentId,
                                                                       UUID projectId,
                                                                       CreateWireDto wire,
                                                                       OvercurrentProtectionType type,
                                                                       int amperage) {
        return new TestCreateOvercurrentProtectionElementDto(
                x,
                y,
                label,
                parentId,
                projectId,
                wire,
                type,
                amperage
        );
    }

    private UpdateOvercurrentProtectionElementDto updateOvercurrentDto(double x,
                                                                       double y,
                                                                       String label,
                                                                       UUID parentId,
                                                                       UpdateWireDto wire,
                                                                       OvercurrentProtectionType type,
                                                                       int amperage) {
        return new TestUpdateOvercurrentProtectionElementDto(
                x,
                y,
                label,
                parentId,
                wire,
                type,
                amperage
        );
    }
}

