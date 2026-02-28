package pl.dabrowski.electrotools.elements.load.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteLoadElementService {
  private final LoadElementRepository loadElementRepository;

  public void deleteAllByIdIn(List<UUID> ids) {
    loadElementRepository.deleteAllByIdInBatch(ids);
  }

  public void deleteAllByProjectIdIn(List<UUID> projectIds) {
    loadElementRepository.deleteByProjectIdIn(projectIds);
  }
}
