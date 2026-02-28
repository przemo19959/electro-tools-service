package pl.dabrowski.electrotools.project.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProjectService {
  private final ProjectRepository projectRepository;
  private final DeleteBasicElementService deleteBasicElementService;

  public void deleteAllById(List<UUID> projectIds) {
    deleteBasicElementService.deleteAllByProjectIdIn(projectIds);
    projectRepository.deleteAllByIdInBatch(projectIds);
  }
}
