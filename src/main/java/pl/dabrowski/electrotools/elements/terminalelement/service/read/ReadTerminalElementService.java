package pl.dabrowski.electrotools.elements.terminalelement.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadTerminalElementService {
  private final TerminalElementRepository terminalElementRepository;

  public List<TerminalElement> findAll(UUID projectId) {
    return terminalElementRepository.findAllByProjectId(projectId);
  }
}
