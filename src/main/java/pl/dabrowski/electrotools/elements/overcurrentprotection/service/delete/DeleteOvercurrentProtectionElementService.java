package pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public void deleteById(UUID overcurrentProtectionElementId) {
    overcurrentProtectionElementRepository.deleteById(overcurrentProtectionElementId);
  }

  public void deleteAllByIdsIn(List<UUID> ids) {
    overcurrentProtectionElementRepository.deleteAllByIdInBatch(ids);
  }

  public void deleteAllByProjectIdIn(List<UUID> projectIds) {
    overcurrentProtectionElementRepository.deleteByProjectIdIn(projectIds);
  }
}
