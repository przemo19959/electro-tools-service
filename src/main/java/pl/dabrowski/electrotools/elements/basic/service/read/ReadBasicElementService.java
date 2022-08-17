package pl.dabrowski.electrotools.elements.basic.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.BasicElement;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadBasicElementService {
  private final BasicElementRepository basicElementRepository;

  public List<ReadBasicElementDto> findAll(UUID projectId) {
    return basicElementRepository.findAllByProjectId(projectId).stream()
        .map(BasicElement::toDto)
        .toList();
  }
}
