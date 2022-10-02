package pl.dabrowski.electrotools.project;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ReadProjectService readProjectService;
  private final CreateProjectService createProjectService;
  private final UpdateProjectService updateProjectService;
  private final DeleteProjectService deleteProjectService;

  @GetMapping
  public ResponseEntity<List<ReadProjectDto>> findAll() {
    return ResponseEntity.ok(readProjectService.findAll());
  }

  @GetMapping("/page")
  public ResponseEntity<Page<ReadProjectDto>> pageAll(Pageable pageable,
                                                      @RequestParam Optional<String> query) {
    return ResponseEntity.ok(readProjectService.pageAll(pageable, query));
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<ReadProjectDto> findById(@PathVariable UUID projectId) {
    return ResponseEntity.ok(readProjectService.findById(projectId));
  }

  @PostMapping
  public ResponseEntity<ReadProjectDto> create(@RequestBody CreateProjectDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createProjectService.create(dto).toDto());
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<ReadProjectDto> update(@PathVariable UUID projectId, @RequestBody UpdateProjectDto dto) {
    return ResponseEntity.ok(updateProjectService.update(projectId, dto).toDto());
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID projectId) {
    deleteProjectService.deleteById(projectId);
    return ResponseEntity.ok().build();
  }
}
