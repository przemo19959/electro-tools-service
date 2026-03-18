package pl.dabrowski.electrotools.project.service.update;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.project.service.exists.ExistProjectService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProjectService {
  private final ProjectRepository projectRepository;
  private final ExistProjectService existProjectService;

  public Project update(UUID projectId, UpdateProjectDto dto) {
    existProjectService.check(projectId, dto.name());

    return projectRepository.findById(projectId)
        .map(v -> v.update(dto)).map(projectRepository::save)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Project with id: " + projectId));
  }
}
