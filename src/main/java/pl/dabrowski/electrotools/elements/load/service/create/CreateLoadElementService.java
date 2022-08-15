package pl.dabrowski.electrotools.elements.load.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateLoadElementService {
  private final LoadElementRepository loadElementRepository;
  private final ProjectRepository projectRepository;

  public LoadElement create(CreateLoadElementDto dto) {
    Project project = projectRepository.getReferenceById(dto.getProjectId());

    return loadElementRepository.save(LoadElement.create(dto, project));
  }
}
