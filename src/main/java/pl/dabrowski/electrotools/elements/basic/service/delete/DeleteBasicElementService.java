package pl.dabrowski.electrotools.elements.basic.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete.DeleteOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.rcdelement.service.delete.DeleteRcdElementService;
import pl.dabrowski.electrotools.elements.terminalelement.service.delete.DeleteTerminalElementService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteBasicElementService {
  private final BasicElementRepository basicElementRepository;
  private final DeleteOvercurrentProtectionElementService deleteOvercurrentProtectionElementService;
  private final DeleteLoadElementService deleteLoadElementService;
  private final DeleteTerminalElementService deleteTerminalElementService;
  private final DeleteRcdElementService deleteRcdElementService;

  public void deleteAllByIdIn(List<UUID> ids) {
    basicElementRepository.deleteAll(basicElementRepository.findAllById(ids));
    deleteOvercurrentProtectionElementService.deleteAllByIdsIn(ids);
    deleteLoadElementService.deleteAllByIdIn(ids);
    deleteTerminalElementService.deleteAllByIdIn(ids);
    deleteRcdElementService.deleteAllByIdIn(ids);
  }

  public void deleteAllByProjectId(UUID projectId) {
    basicElementRepository.deleteAll(basicElementRepository.findAllByProjectId(projectId));

    deleteOvercurrentProtectionElementService.deleteAllByProjectId(projectId);
    deleteLoadElementService.deleteAllByProjectId(projectId);
    deleteTerminalElementService.deleteAllByProjectId(projectId);
    deleteRcdElementService.deleteAllByProjectId(projectId);
  }
}
