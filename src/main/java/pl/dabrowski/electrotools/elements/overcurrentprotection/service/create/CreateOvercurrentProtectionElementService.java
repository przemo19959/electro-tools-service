package pl.dabrowski.electrotools.elements.overcurrentprotection.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;
  private final ProjectRepository projectRepository;

  public OvercurrentProtectionElement create(CreateOvercurrentProtectionElementDto dto) {
    Project project = projectRepository.getReferenceById(dto.getProjectId());

    return overcurrentProtectionElementRepository.save(OvercurrentProtectionElement.create(dto, project));
  }
}
