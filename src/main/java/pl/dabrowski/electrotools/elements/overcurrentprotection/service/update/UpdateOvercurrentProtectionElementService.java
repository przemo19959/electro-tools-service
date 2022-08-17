package pl.dabrowski.electrotools.elements.overcurrentprotection.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public OvercurrentProtectionElement update(UUID overcurrentProtectionElementId, UpdateOvercurrentProtectionElementDto dto) {
    return overcurrentProtectionElementRepository.findById(overcurrentProtectionElementId)
        .map(v -> v.update(dto))
        .map(overcurrentProtectionElementRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No OvercurrentProtectionElement with id: " + overcurrentProtectionElementId + ""));
  }
}
