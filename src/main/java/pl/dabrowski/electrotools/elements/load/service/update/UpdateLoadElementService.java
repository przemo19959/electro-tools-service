package pl.dabrowski.electrotools.elements.load.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateLoadElementService {
  private final LoadElementRepository loadElementRepository;

  public LoadElement update(UUID loadElementId, UpdateLoadElementDto dto) {
    return loadElementRepository.findById(loadElementId)
        .map(v -> v.update(dto))
        .map(loadElementRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No LoadElement with id: " + loadElementId + ""));
  }
}
