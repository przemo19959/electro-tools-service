package pl.dabrowski.electrotools.project.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateProjectService {
  private final ProjectRepository projectRepository;

  public Project create(CreateProjectDto dto) {
    return projectRepository.save(Project.create(dto));
  }
}
