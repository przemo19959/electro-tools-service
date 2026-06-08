package pl.dabrowski.electrotools.project;

import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.filter.FilterGroupDto;
import pl.dabrowski.electrotools.filter.column.ProjectFilterableColumn;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.create.CreateProjectService;
import pl.dabrowski.electrotools.project.service.delete.DeleteProjectService;
import pl.dabrowski.electrotools.project.service.read.ReadProjectDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectService;

import java.util.*;

@RestController
@RequestMapping(ProjectController.BASE_URL)
@RequiredArgsConstructor
public class ProjectController {
    public static final String BASE_URL = "/api/v1/projects";
    public static final FunctionDeclaration PROJECT_FIND_ALL_TOOL = FunctionDeclaration.builder()
            .name("findAll")
            .description("Find all projects")
            .build();
    public static final FunctionDeclaration PROJECT_PAGE_ALL_TOOL = FunctionDeclaration.builder()
            .name("pageAll")
            .description("Page all projects with optional query and filter")
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "page",
                            Schema.builder()
                                    .type(Type.Known.INTEGER)
                                    .description("Page number starting from 0")
                                    .build(),

                            "size",
                            Schema.builder()
                                    .type(Type.Known.INTEGER)
                                    .description("Number of results per page")
                                    .build(),

                            "query",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Search text")
                                    .build()
                    ))
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_FIND_BY_ID_TOOL = FunctionDeclaration.builder()
            .name("findById")
            .description("Find project by id")
            .parameters(Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Project id as UUID string")
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_FIND_DISTINCT_VALUES_TOOL = FunctionDeclaration.builder()
            .name("findDistinctValues")
            .description("Find distinct values for a given column")
            .parameters(Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Column name to find distinct values for")
                    .format("enum")
                    .enum_(Arrays.stream(ProjectFilterableColumn.values()).map(ProjectFilterableColumn::name).toList())
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_CREATE_TOOL = FunctionDeclaration.builder()
            .name("create")
            .description("Create a new project")
            .parameters(Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Name of the project")
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_UPDATE_TOOL = FunctionDeclaration.builder()
            .name("update")
            .description("Update an existing project")
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "name",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Name of the project")
                                    .build()
                    )).build())
            .build();
    public static final FunctionDeclaration PROJECT_DELETE_ALL_BY_ID_TOOL = FunctionDeclaration.builder()
            .name("deleteAllById")
            .description("Delete projects by a list of ids")
            .parameters(Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(Schema.builder()
                            .type(Type.Known.STRING)
                            .format("uuid")
                            .description("Project id as UUID string")
                            .build())
                    .description("List of project ids to delete")
                    .build())
            .build();

    private final ReadProjectService readProjectService;
    private final CreateProjectService createProjectService;
    private final UpdateProjectService updateProjectService;
    private final DeleteProjectService deleteProjectService;

    @GetMapping
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<List<ReadProjectDto>> findAll() {
        return ResponseEntity.ok(readProjectService.findAll());
    }

    @PostMapping("/page")
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<Page<ReadProjectDto>> pageAll(@ParameterObject Pageable pageable,
                                                        @RequestParam Optional<String> query,
                                                        @RequestBody FilterGroupDto filter
    ) {
        return ResponseEntity.ok(readProjectService.pageAll(pageable, query, filter));
    }

    @GetMapping("/{projectId}")
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<ReadProjectDto> findById(@PathVariable UUID projectId) {
        return ResponseEntity.ok(readProjectService.findById(projectId));
    }

    @GetMapping("/distinct-values")
    public ResponseEntity<List<String>> findDistinctValues(@RequestParam String column) {
        return ResponseEntity.ok(readProjectService.findDistinctValues(column));
    }

    @PostMapping
//  @PreAuthorize("hasAuthority('edit_projects')")
    public ResponseEntity<ReadProjectDto> create(@Valid @RequestBody CreateProjectDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createProjectService.create(dto).toDto());
    }

    @PutMapping("/{projectId}")
//  @PreAuthorize("hasAuthority('edit_projects')")
    public ResponseEntity<ReadProjectDto> update(@PathVariable UUID projectId, @Valid @RequestBody UpdateProjectDto dto) {
        return ResponseEntity.ok(updateProjectService.update(projectId, dto).toDto());
    }

    @DeleteMapping
//  @PreAuthorize("hasAuthority('edit_projects')")
    public ResponseEntity<Void> deleteAllById(@RequestBody List<UUID> projectIds) {
        deleteProjectService.deleteAllById(projectIds);
        return ResponseEntity.ok().build();
    }
}
