package pl.dabrowski.electrotools.elements.basic.service.update;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.BasicElement;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementService;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementService;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateBasicElementService {
  private final BasicElementRepository basicElementRepository;
  private final UpdateLoadElementService updateLoadElementService;
  private final UpdateOvercurrentProtectionElementService updateOvercurrentProtectionElementService;
  private final UpdateTerminalElementService updateTerminalElementService;
  private final UpdateRcdElementService updateRcdElementService;

  public BasicElement update(UUID elementId, UpdateAbstractElementDto dto) {
    return basicElementRepository.findById(elementId)
        .map(v -> v.update(dto))
        .map(basicElementRepository::save)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No BasicElement with id: " + elementId + ""));
  }

  public void updatePositions(List<UpdateBasicElementPositionDto> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    var ids = changes.stream()
            .map(UpdateBasicElementPositionDto::elementId)
        .collect(Collectors.toSet());

    var elementsById = basicElementRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(BasicElement::getId, Function.identity()));

    for (var change : changes) {
      var element = elementsById.get(change.elementId());
      if (element != null) {
        element.updatePosition(change.x(), change.y());
      }
    }

    basicElementRepository.saveAll(elementsById.values());
    
    updateLoadElementService.updatePositions(changes);
    updateOvercurrentProtectionElementService.updatePositions(changes);
    updateTerminalElementService.updatePositions(changes);
    updateRcdElementService.updatePositions(changes);
  }
}
