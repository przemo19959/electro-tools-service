package pl.dabrowski.electrotools.elements.rcdelement.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteRcdElementService {
  private final RcdElementRepository rcdElementRepository;

  public void deleteAllByIdIn(List<UUID> ids) {
    rcdElementRepository.deleteAllByIdInBatch(ids);
  }

  public void deleteAllByProjectIdIn(List<UUID> projectIds) {
    rcdElementRepository.deleteByProjectIdIn(projectIds);
  }
}
