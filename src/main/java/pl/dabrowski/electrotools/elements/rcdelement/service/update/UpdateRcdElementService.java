package pl.dabrowski.electrotools.elements.rcdelement.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElement;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRcdElementService {
  private final List<Integer> legalPoleNumbers = List.of(2, 4);
  private final RcdElementRepository rcdElementRepository;

  public RcdElement update(UUID rcdElementId, UpdateRcdElementDto dto) {
    if (!legalPoleNumbers.contains(dto.getPoleNumber())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "RCD element pole number must be 2 or 4");
    }

    return rcdElementRepository.findById(rcdElementId)
        .map(v -> v.update(dto))
        .map(rcdElementRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No RcdElement with id: " + rcdElementId + ""));
  }
}
