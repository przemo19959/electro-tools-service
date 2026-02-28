package pl.dabrowski.electrotools.project.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProjectService {
  private final ProjectRepository projectRepository;
  private final DeleteBasicElementService deleteBasicElementService;

  public void deleteById(UUID projectId) {
    deleteBasicElementService.deleteAllByProjectId(projectId);
    projectRepository.deleteById(projectId);
  }
}
