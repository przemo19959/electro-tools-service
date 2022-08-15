package pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteOvercurrentProtectionElementService {

  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public void deleteById(UUID overcurrentProtectionElementId) {
    overcurrentProtectionElementRepository.deleteById(overcurrentProtectionElementId);
  }
}
