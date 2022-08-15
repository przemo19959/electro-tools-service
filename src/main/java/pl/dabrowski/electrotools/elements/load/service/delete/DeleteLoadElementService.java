package pl.dabrowski.electrotools.elements.load.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteLoadElementService {

  private final LoadElementRepository loadElementRepository;

  public void deleteById(UUID loadElementId) {
    loadElementRepository.deleteById(loadElementId);
  }
}
