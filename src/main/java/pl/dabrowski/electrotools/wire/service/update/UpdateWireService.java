package pl.dabrowski.electrotools.wire.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.wire.Wire;
import pl.dabrowski.electrotools.wire.repository.WireRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateWireService {
  private final WireRepository wireRepository;

  public Wire update(UpdateWireDto dto) {
    return wireRepository.findById(dto.getId())
        .map(v -> v.update(dto))
        .map(wireRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No Wire with id: " + dto.getId() + ""));
  }
}
