package pl.dabrowski.electrotools.elements.basic.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete.DeleteOvercurrentProtectionElementService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteBasicElementService {
  private final DeleteOvercurrentProtectionElementService deleteOvercurrentProtectionElementService;
  private final DeleteLoadElementService deleteLoadElementService;

  public void deleteAllByIdsIn(List<UUID> ids) {
    deleteOvercurrentProtectionElementService.deleteAllByIdsIn(ids);
    deleteLoadElementService.deleteAllByIdIn(ids);
  }
}
