package pl.dabrowski.electrotools.elements.overcurrentprotection.service.update;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public OvercurrentProtectionElement update(UUID overcurrentProtectionElementId, UpdateOvercurrentProtectionElementDto dto) {
    return overcurrentProtectionElementRepository.findById(overcurrentProtectionElementId)
        .map(v -> v.update(dto))
        .map(overcurrentProtectionElementRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No OvercurrentProtectionElement with id: " + overcurrentProtectionElementId + ""));
  }

  public void updatePositions(List<UpdateBasicElementPositionDto> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    var ids = changes.stream()
            .map(UpdateBasicElementPositionDto::elementId)
        .collect(Collectors.toSet());

    var elementsById = overcurrentProtectionElementRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(OvercurrentProtectionElement::getId, Function.identity()));

    for (var change : changes) {
      var element = elementsById.get(change.elementId());
      if (element != null) {
        element.updatePosition(change.x(), change.y());
      }
    }

    overcurrentProtectionElementRepository.saveAll(elementsById.values());
  }
}
