package pl.dabrowski.electrotools.elements.basic.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.BasicElement;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateBasicElementService {
  private final BasicElementRepository basicElementRepository;

  public BasicElement update(UUID loadElementId, UpdateAbstractElementDto dto) {
    return basicElementRepository.findById(loadElementId)
        .map(v -> v.update(dto))
        .map(basicElementRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No BasicElement with id: " + loadElementId + ""));
  }
}
