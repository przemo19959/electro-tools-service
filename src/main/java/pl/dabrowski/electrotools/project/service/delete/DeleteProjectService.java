package pl.dabrowski.electrotools.project.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProjectService {
  private final ProjectRepository projectRepository;

  // TODO 30.01.2023 pdabrowski: usunąć powiązane z projektem elementy + zapytanie z ostrzeżeniem jeśli istnieją projekty
  public void deleteById(UUID projectId) {
    projectRepository.deleteById(projectId);
  }
}
