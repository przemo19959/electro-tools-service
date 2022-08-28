package pl.dabrowski.electrotools.elements.overcurrentprotection.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public List<OvercurrentProtectionElement> findAll(UUID projectId) {
    return overcurrentProtectionElementRepository.findAllByProjectId(projectId);
  }
}
