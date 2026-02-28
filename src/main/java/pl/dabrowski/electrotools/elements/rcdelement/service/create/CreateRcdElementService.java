package pl.dabrowski.electrotools.elements.rcdelement.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElement;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRcdElementService {
  private final List<Integer> legalPoleNumbers = List.of(2, 4);
  private final RcdElementRepository rcdElementRepository;
  private final ProjectRepository projectRepository;

  public RcdElement create(CreateRcdElementDto dto) {
    if (!legalPoleNumbers.contains(dto.getPoleNumber())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "RCD element pole number must be 2 or 4");
    }

    Project project = projectRepository.getReferenceById(dto.getProjectId());
    return rcdElementRepository.save(RcdElement.create(dto, project));
  }
}
