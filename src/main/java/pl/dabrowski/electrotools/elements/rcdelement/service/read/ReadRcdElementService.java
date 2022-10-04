package pl.dabrowski.electrotools.elements.rcdelement.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElement;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadRcdElementService {
  private final RcdElementRepository rcdElementRepository;

  public List<RcdElement> findAll(UUID projectId) {
    return rcdElementRepository.findAllByProjectId(projectId);
  }
}
