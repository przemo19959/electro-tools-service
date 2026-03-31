package pl.dabrowski.electrotools.project;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.dabrowski.electrotools.IntegrationTest;
import pl.dabrowski.electrotools.PageResponse;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectController Integration Tests")
class ProjectControllerTest extends IntegrationTest {

    private String projectId;

    @Autowired
    private ProjectRepository projectRepository;

    private final CreateProjectDto defaultCreateDto = new CreateProjectDto("Test Project");

    @BeforeEach
    void setUp() {
        projectId = projectApi()
                .create(defaultCreateDto)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Should return all projects")
        void testFindAll_Success() {
            List<ReadProjectDto> projects = projectApi()
                    .findAll()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(projects).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list when no projects exist")
        void testFindAll_EmptyList() {
            projectApi().deleteAllById(List.of(UUID.fromString(projectId)));

            List<ReadProjectDto> projects = projectApi()
                    .findAll()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(projects).isEmpty();
        }

        @Test
        @DisplayName("Should return projects with correct structure")
        void testFindAll_CorrectStructure() {
            List<ReadProjectDto> projects = projectApi()
                    .findAll()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(projects).isNotEmpty();
            ReadProjectDto firstProject = projects.getFirst();
            assertThat(firstProject.id()).isNotNull();
            assertThat(firstProject.name()).isEqualTo(defaultCreateDto.name());
            assertThat(firstProject.createdBy()).isEqualTo("system");
            assertThat(firstProject.elementCount()).isZero();
        }
    }

    @Nested
    @DisplayName("pageAll(int page, int size, String query)")
    class PageAllTests {

        @Test
        @DisplayName("Should return paginated projects with default page and size")
        void testPageAll_DefaultPagination() {
            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            List<ReadProjectDto> content = response.getContent();
            assertThat(content).isNotNull().hasSize(1);

            ReadProjectDto firstItem = content.getFirst();
            assertThat(firstItem.id()).isNotNull();
            assertThat(firstItem.name()).isEqualTo(defaultCreateDto.name());
            assertThat(firstItem.createdBy()).isEqualTo("system");
            assertThat(firstItem.elementCount()).isZero();


            assertThat(response.getNumber()).isZero();
            assertThat(response.getSize()).isEqualTo(10);
            assertThat(response.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return correct page when requesting second page")
        void testPageAll_SecondPage() {
            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(1, 5)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            List<ReadProjectDto> content = response.getContent();
            assertThat(content).isNotNull().isEmpty();

            assertThat(response.getNumber()).isEqualTo(1);
            assertThat(response.getSize()).isEqualTo(5);
            assertThat(response.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return single item per page when size is 1")
        void testPageAll_SingleItemPerPage() {
            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 1)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            List<ReadProjectDto> content = response.getContent();
            assertThat(content).isNotNull();

            assertThat(response.getNumber()).isZero();
            assertThat(response.getSize()).isEqualTo(1);
            assertThat(response.getTotalElements()).isEqualTo(1);

            ReadProjectDto firstItem = content.getFirst();
            assertThat(firstItem.id()).isNotNull();
            assertThat(firstItem.name()).isEqualTo(defaultCreateDto.name());
            assertThat(firstItem.createdBy()).isEqualTo("system");
            assertThat(firstItem.elementCount()).isZero();
        }

        @Test
        @DisplayName("Should return large page size when requested")
        void testPageAll_LargePageSize() {
            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 100)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getSize()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should return empty content for out-of-range page")
        void testPageAll_OutOfRangePage() {
            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(999, 10)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getContent()).isEmpty();
        }

        @Test
        @DisplayName("Should return filtered projects when query matches name")
        void testPageAll_WithQuery() {
            CreateProjectDto matchingDto = new CreateProjectDto("Matching Project");
            CreateProjectDto otherDto = new CreateProjectDto("Other Project");

            projectApi().create(matchingDto).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(otherDto).then().statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, matchingDto.name())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getContent()).hasSize(1);
            assertThat(response.getTotalElements()).isEqualTo(1);

            ReadProjectDto firstItem = response.getContent().getFirst();
            assertThat(firstItem.name()).isEqualTo(matchingDto.name());
        }

        @Test
        @DisplayName("Should return empty page when query does not match any project")
        void testPageAll_WithQueryNoMatch() {
            String query = "missing-project";

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getContent()).isEmpty();
            assertThat(response.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should return filtered project when query matches part of name ignoring case")
        void testPageAll_WithQueryMatchingNamePartiallyIgnoringCase() {
            CreateProjectDto matchingDto = new CreateProjectDto("Main Distribution Board");
            CreateProjectDto otherDto = new CreateProjectDto("Garage Socket Circuit");
            String query = "distribution";

            projectApi().create(matchingDto).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(otherDto).then().statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query.toUpperCase())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isEqualTo(1);
            assertThat(response.getContent())
                    .singleElement()
                    .satisfies(project -> assertThat(project.name()).isEqualTo(matchingDto.name()));
        }

        @Test
        @DisplayName("Should return all projects when query matches multiple names")
        void testPageAll_WithQueryMatchingMultipleNames() {
            String query = "Query Group";
            CreateProjectDto firstMatchingDto = new CreateProjectDto(query + " Alpha");
            CreateProjectDto secondMatchingDto = new CreateProjectDto(query + " Beta");
            CreateProjectDto otherDto = new CreateProjectDto("Other Project");

            projectApi().create(firstMatchingDto).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(secondMatchingDto).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(otherDto).then().statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query.toLowerCase())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isEqualTo(2);
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::name)
                    .containsExactlyInAnyOrder(firstMatchingDto.name(), secondMatchingDto.name());
        }

        @Test
        @DisplayName("Should return filtered project when query matches createdBy ignoring case")
        void testPageAll_WithQueryMatchingCreatedByIgnoringCase() {
            CreateProjectDto dtoOne = new CreateProjectDto("CreatedBy Filter A");
            CreateProjectDto dtoTwo = new CreateProjectDto("CreatedBy Filter B");
            projectApi().create(dtoOne).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(dtoTwo).then().statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, "SYSTEM")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isEqualTo(3);
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::createdBy)
                    .containsOnly("system");
        }

        @Test
        @DisplayName("Should return filtered project when query matches modifiedBy ignoring case")
        void testPageAll_WithQueryMatchingModifiedByIgnoringCase() {
            CreateProjectDto dto = new CreateProjectDto("ModifiedBy Filter Project Updated");
            projectApi()
                    .create(dto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, "SyStEm")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getContent())
                    .extracting(ReadProjectDto::modifiedBy)
                    .containsOnly("system");
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::name)
                    .contains(dto.name());
        }

        @Test
        @DisplayName("Should return filtered project when query matches modifiedDate exactly")
        void testPageAll_WithQueryMatchingModifiedDateExactly() {
            CreateProjectDto dto = new CreateProjectDto("Modified Date Exact Match");
            String createdId = projectApi()
                    .create(dto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            ReadProjectDto createdProject = projectApi()
                    .findById(createdId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            String query = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneOffset.UTC)
                    .format(createdProject.modifiedDate());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isGreaterThanOrEqualTo(1);
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::name)
                    .contains(dto.name());
        }

        @Test
        @DisplayName("Should return filtered projects when query matches modifiedDate partially")
        void testPageAll_WithQueryMatchingModifiedDatePartially() {
            CreateProjectDto firstDto = new CreateProjectDto("Modified Date Match One");
            CreateProjectDto secondDto = new CreateProjectDto("Modified Date Match Two");
            projectApi().create(firstDto).then().statusCode(HttpStatus.CREATED.value());
            String secondId = projectApi()
                    .create(secondDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            ReadProjectDto secondProject = projectApi()
                    .findById(secondId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            Instant modifiedDate = secondProject.modifiedDate();
            String query = DateTimeFormatter.ofPattern("yyyy-MM")
                    .withZone(ZoneOffset.UTC)
                    .format(modifiedDate);

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isGreaterThanOrEqualTo(2);
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::name)
                    .contains(firstDto.name(), secondDto.name());
        }

        @Test
        @DisplayName("Should return projects matched across different fields")
        void testPageAll_WithQueryMatchingAcrossDifferentFields() {
            String query = "sys";
            CreateProjectDto nameMatchingDto = new CreateProjectDto("Syst Named Project");
            CreateProjectDto otherDto = new CreateProjectDto("Other Project");
            projectApi().create(nameMatchingDto).then().statusCode(HttpStatus.CREATED.value());
            projectApi().create(otherDto).then().statusCode(HttpStatus.CREATED.value());

            PageResponse<ReadProjectDto> response = projectApi()
                    .pageAll(0, 10, query)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(new TypeRef<>() {
                    });

            assertThat(response.getTotalElements()).isEqualTo(3);
            assertThat(response.getContent())
                    .extracting(ReadProjectDto::name)
                    .contains(defaultCreateDto.name(), nameMatchingDto.name(), otherDto.name());
        }
    }

    @Nested
    @DisplayName("findById(String projectId)")
    class FindByIdTests {

        @Test
        @DisplayName("Should find project by valid ID")
        void testFindById_ValidId() {
            ReadProjectDto project = projectApi()
                    .findById(projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.id()).hasToString(projectId);
            assertThat(project.name()).isEqualTo(defaultCreateDto.name());
            assertThat(project.createdBy()).isNotNull();
            assertThat(project.elementCount()).isZero();
        }

        @Test
        @DisplayName("Should return 404 when project ID doesn't exist")
        void testFindById_InvalidId() {
            UUID nonExistentId = UUID.randomUUID();

            projectApi()
                    .findById(nonExistentId.toString())
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("create(Object body)")
    class CreateTests {

        @Test
        @DisplayName("Should create project with valid data")
        void testCreate_ValidData() {
            CreateProjectDto createDto = new CreateProjectDto("New Project");

            ReadProjectDto project = projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.id()).isNotNull();
            assertThat(project.name()).isEqualTo("New Project");
            assertThat(project.createdBy()).isNotNull();
            assertThat(project.elementCount()).isZero();
        }

        @Test
        @DisplayName("Should return 400 when creating project with null name")
        void testCreate_NullName() {
            CreateProjectDto createDto = new CreateProjectDto(null);

            projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should return 400 when creating project with empty name")
        void testCreate_EmptyName() {
            CreateProjectDto createDto = new CreateProjectDto("");

            projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should create project with long name")
        void testCreate_LongName() {
            String longName = "A".repeat(255);
            CreateProjectDto createDto = new CreateProjectDto(longName);

            ReadProjectDto project = projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.name()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should create project with special characters in name")
        void testCreate_SpecialCharacters() {
            String nameWithSpecialChars = "Project #123 @ Test-Name_2025";
            CreateProjectDto createDto = new CreateProjectDto(nameWithSpecialChars);

            ReadProjectDto project = projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.name()).isEqualTo(nameWithSpecialChars);
        }

        @Test
        @DisplayName("Should return 409 when creating project with existing name")
        void testCreate_ExistingName() {
            CreateProjectDto duplicateDto = new CreateProjectDto(defaultCreateDto.name());

            projectApi()
                    .create(duplicateDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("update(String projectId, Object body)")
    class UpdateTests {

        @Test
        @DisplayName("Should update project with valid data")
        void testUpdate_ValidData() {
            UpdateProjectDto updateDto = new UpdateProjectDto("Updated Project Name");

            ReadProjectDto project = projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.id()).hasToString(projectId);
            assertThat(project.name()).isEqualTo("Updated Project Name");
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent project")
        void testUpdate_NonExistentProject() {
            UUID nonExistentId = UUID.randomUUID();
            UpdateProjectDto updateDto = new UpdateProjectDto("Updated Name");

            projectApi()
                    .update(nonExistentId.toString(), updateDto)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should return 400 when updating with null name")
        void testUpdate_NullName() {
            UpdateProjectDto updateDto = new UpdateProjectDto(null);

            projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should return 400 when updating with empty name")
        void testUpdate_EmptyName() {
            UpdateProjectDto updateDto = new UpdateProjectDto("");

            projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should preserve ID after update")
        void testUpdate_PreserveId() {
            UpdateProjectDto updateDto = new UpdateProjectDto("Updated Project");

            ReadProjectDto project = projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.id()).hasToString(projectId);
        }

        @Test
        @DisplayName("Should update project with special characters")
        void testUpdate_SpecialCharacters() {
            String nameWithSpecialChars = "Updated #Project @ 2025_Test-Name";
            UpdateProjectDto updateDto = new UpdateProjectDto(nameWithSpecialChars);

            ReadProjectDto project = projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.name()).isEqualTo(nameWithSpecialChars);
        }

        @Test
        @DisplayName("Should return 409 when updating to existing name")
        void testUpdate_ExistingName() {
            projectApi()
                    .create(new CreateProjectDto("Other Project"))
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            UpdateProjectDto updateDto = new UpdateProjectDto("Other Project");

            projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }

        @Test
        @DisplayName("Should allow updating to same name")
        void testUpdate_SameName() {
            UpdateProjectDto updateDto = new UpdateProjectDto(defaultCreateDto.name());

            ReadProjectDto project = projectApi()
                    .update(projectId, updateDto)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.name()).isEqualTo(defaultCreateDto.name());
        }
    }

    @Nested
    @DisplayName("deleteAllById(List<UUID> projectIds)")
    class DeleteTests {

        @Test
        @DisplayName("Should delete single project by ID")
        void testDeleteAllById_SingleProject() {
            CreateProjectDto createDto = new CreateProjectDto("Project to Delete");

            String idToDelete = projectApi()
                    .create(createDto)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            projectApi()
                    .deleteAllById(List.of(UUID.fromString(idToDelete)))
                    .then()
                    .statusCode(HttpStatus.OK.value());

            projectApi()
                    .findById(idToDelete)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should delete multiple projects by IDs")
        void testDeleteAllById_MultipleProjects() {
            String id1 = projectApi()
                    .create(new CreateProjectDto("Delete Test 1"))
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            String id2 = projectApi()
                    .create(new CreateProjectDto("Delete Test 2"))
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            projectApi()
                    .deleteAllById(List.of(UUID.fromString(id1), UUID.fromString(id2)))
                    .then()
                    .statusCode(HttpStatus.OK.value());

            projectApi()
                    .findById(id1)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());

            projectApi()
                    .findById(id2)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should handle delete with empty list")
        void testDeleteAllById_EmptyList() {
            projectApi()
                    .deleteAllById(List.of())
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should handle delete with non-existent project IDs")
        void testDeleteAllById_NonExistentIds() {
            List<UUID> nonExistentIds = List.of(
                    UUID.randomUUID(),
                    UUID.randomUUID()
            );

            projectApi()
                    .deleteAllById(nonExistentIds)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Should delete without affecting other projects")
        void testDeleteAllById_PreservesOtherProjects() {
            String projectToKeepId = projectApi()
                    .create(new CreateProjectDto("Project to Keep"))
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id")
                    .toString();

            projectApi()
                    .deleteAllById(List.of(UUID.fromString(projectId)))
                    .then()
                    .statusCode(HttpStatus.OK.value());

            ReadProjectDto project = projectApi()
                    .findById(projectToKeepId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ReadProjectDto.class);

            assertThat(project.id()).hasToString(projectToKeepId);
        }
    }
}
