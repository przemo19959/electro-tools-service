package pl.dabrowski.electrotools.elements.overcurrentprotection.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadOvercurrentProtectionElementService {
  private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;

  public List<ReadOvercurrentProtectionElementDto> findAll(UUID projectId) {
    return overcurrentProtectionElementRepository.findAllByProjectId(projectId).stream()
        .map(OvercurrentProtectionElement::toDto)
        .toList();
  }

  public Page<ReadOvercurrentProtectionElementDto> pageAll(Pageable pageable) {
    return overcurrentProtectionElementRepository.findAll(pageable).map(OvercurrentProtectionElement::toDto);
  }

  public ReadOvercurrentProtectionElementDto findById(UUID overcurrentProtectionElementId) {
    return overcurrentProtectionElementRepository.findById(overcurrentProtectionElementId)
        .map(OvercurrentProtectionElement::toDto)
        .orElseThrow(() -> new NoSuchElementException("No OvercurrentProtectionElement with id: " + overcurrentProtectionElementId + ""));
  }
}
