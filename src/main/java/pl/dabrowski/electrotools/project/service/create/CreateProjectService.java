package pl.dabrowski.electrotools.project.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.project.service.exists.ExistProjectService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateProjectService {
  private final ProjectRepository projectRepository;
  private final ExistProjectService existProjectService;

  public Project create(CreateProjectDto dto) {
    existProjectService.check(dto.getName());

    return projectRepository.save(Project.create(dto));
  }
}
