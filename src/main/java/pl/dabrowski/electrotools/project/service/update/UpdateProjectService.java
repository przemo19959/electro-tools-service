package pl.dabrowski.electrotools.project.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.project.service.exists.ExistProjectService;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProjectService {
  private final ProjectRepository projectRepository;
  private final ExistProjectService existProjectService;

  public Project update(UUID projectId, UpdateProjectDto dto) {
    existProjectService.check(projectId, dto.getName());

    return projectRepository.findById(projectId)
        .map(v -> v.update(dto)).map(projectRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No Project with id: " + projectId + ""));
  }
}
