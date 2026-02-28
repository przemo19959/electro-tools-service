package pl.dabrowski.electrotools.elements.terminalelement.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteTerminalElementService {
  private final TerminalElementRepository terminalElementRepository;

  public void deleteAllByIdIn(List<UUID> ids) {
    terminalElementRepository.deleteAllByIdInBatch(ids);
  }

  public void deleteAllByProjectIdIn(List<UUID> projectIds) {
    terminalElementRepository.deleteByProjectIdIn(projectIds);
  }
}
