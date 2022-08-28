package pl.dabrowski.electrotools.elements.load.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadLoadElementService {
  private final LoadElementRepository loadElementRepository;

  public List<LoadElement> findAll(UUID projectId) {
    return loadElementRepository.findAllByProjectId(projectId);
  }
}
