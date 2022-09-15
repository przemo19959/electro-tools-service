package pl.dabrowski.electrotools.elements.terminalelement.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

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
        .orElseThrow(() -> new NoSuchElementException("No TerminalElement with id: " + terminalElementId + ""));
  }
}
