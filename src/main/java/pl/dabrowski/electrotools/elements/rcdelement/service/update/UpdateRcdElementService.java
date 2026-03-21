package pl.dabrowski.electrotools.elements.rcdelement.service.update;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElement;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRcdElementService {
  private final List<Integer> legalPoleNumbers = List.of(2, 4);
  private final RcdElementRepository rcdElementRepository;

  public RcdElement update(UUID rcdElementId, UpdateRcdElementDto dto) {
    if (!legalPoleNumbers.contains(dto.getPoleNumber())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "RCD element pole number must be 2 or 4");
    }

    return rcdElementRepository.findById(rcdElementId)
        .map(v -> v.update(dto))
        .map(rcdElementRepository::save)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No RcdElement with id: " + rcdElementId));
  }

  public void updatePositions(List<UpdateBasicElementPositionDto> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    var ids = changes.stream()
            .map(UpdateBasicElementPositionDto::elementId)
        .collect(Collectors.toSet());

    var elementsById = rcdElementRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(RcdElement::getId, Function.identity()));

    for (var change : changes) {
      var element = elementsById.get(change.elementId());
      if (element != null) {
        element.updatePosition(change.x(), change.y());
      }
    }

    rcdElementRepository.saveAll(elementsById.values());
  }
}
