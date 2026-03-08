package pl.dabrowski.electrotools.elements.load.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  public void updatePositions(List<UpdateBasicElementPositionDto> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    var ids = changes.stream()
        .map(UpdateBasicElementPositionDto::getElementId)
        .collect(Collectors.toSet());

    var elementsById = loadElementRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(LoadElement::getId, Function.identity()));

    for (var change : changes) {
      var element = elementsById.get(change.getElementId());
      if (element != null) {
        element.updatePosition(change.getX(), change.getY());
      }
    }

    loadElementRepository.saveAll(elementsById.values());
  }
}
