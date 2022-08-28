package pl.dabrowski.electrotools.elements.basic.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.BasicElement;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateBasicElementService {
  private final BasicElementRepository basicElementRepository;
  private final ProjectRepository projectRepository;

  public BasicElement create(CreateAbstractElementDto dto) {
    Project project = projectRepository.getReferenceById(dto.getProjectId());

    return basicElementRepository.save(BasicElement.create(dto, project));
  }
}
