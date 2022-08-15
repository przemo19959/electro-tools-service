package pl.dabrowski.electrotools.wire.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.wire.Wire;
import pl.dabrowski.electrotools.wire.repository.WireRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateWireService {

  private final WireRepository wireRepository;

  public Wire update(UUID wireId, UpdateWireDto dto) {
    return wireRepository.findById(wireId).map(v -> v.update(dto)).map(wireRepository::save).orElseThrow(() -> new NoSuchElementException("No Wire with id: " + wireId + ""));
  }
}
