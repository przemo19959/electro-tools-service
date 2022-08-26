package pl.dabrowski.electrotools.elements.basic.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.service.delete.DeleteConnectionService;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete.DeleteOvercurrentProtectionElementService;

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
  private final DeleteConnectionService deleteConnectionService;

  public void deleteAllByIdIn(List<UUID> ids) {
    basicElementRepository.deleteAll(basicElementRepository.findAllById(ids));
    deleteOvercurrentProtectionElementService.deleteAllByIdsIn(ids);
    deleteLoadElementService.deleteAllByIdIn(ids);
  }

  public void deleteAllByIdInWithConnections(List<UUID> ids) {
    deleteConnectionService.deleteAllByElementIdIn(ids);
    deleteAllByIdIn(ids);
  }
}
