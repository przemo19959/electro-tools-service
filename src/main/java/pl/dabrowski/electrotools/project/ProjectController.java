package pl.dabrowski.electrotools.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.create.CreateProjectService;
import pl.dabrowski.electrotools.project.service.delete.DeleteProjectService;
import pl.dabrowski.electrotools.project.service.read.ReadProjectDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ProjectController.BASE_URL)
@RequiredArgsConstructor
public class ProjectController {
    public static final String BASE_URL = "/api/v1/projects";

    private final ReadProjectService readProjectService;
    private final CreateProjectService createProjectService;
    private final UpdateProjectService updateProjectService;
    private final DeleteProjectService deleteProjectService;

    @GetMapping
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<List<ReadProjectDto>> findAll() {
        return ResponseEntity.ok(readProjectService.findAll());
    }

    @GetMapping("/page")
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<Page<ReadProjectDto>> pageAll(@ParameterObject Pageable pageable,
                                                        @RequestParam Optional<String> query) {
        return ResponseEntity.ok(readProjectService.pageAll(pageable, query));
    }

    @GetMapping("/{projectId}")
//  @PreAuthorize("hasAuthority('read_projects')")
    public ResponseEntity<ReadProjectDto> findById(@PathVariable UUID projectId) {
        return ResponseEntity.ok(readProjectService.findById(projectId));
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
