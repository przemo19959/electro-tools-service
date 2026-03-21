package pl.dabrowski.electrotools.elements.terminalelement.service.update;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateTerminalElementService {
  private final TerminalElementRepository terminalElementRepository;

  public TerminalElement update(UUID terminalElementId, UpdateTerminalElementDto dto) {
    if (dto.getParentId() != null || dto.getWire() != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal element must not have parents");
    }

    return terminalElementRepository.findById(terminalElementId)
        .map(v -> v.update(dto))
        .map(terminalElementRepository::save)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No TerminalElement with id: " + terminalElementId));
  }

  public void updatePositions(List<UpdateBasicElementPositionDto> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    var ids = changes.stream()
            .map(UpdateBasicElementPositionDto::elementId)
        .collect(Collectors.toSet());

    var elementsById = terminalElementRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(TerminalElement::getId, Function.identity()));

    for (var change : changes) {
      var element = elementsById.get(change.elementId());
      if (element != null) {
        element.updatePosition(change.x(), change.y());
      }
    }

    terminalElementRepository.saveAll(elementsById.values());
  }
}
