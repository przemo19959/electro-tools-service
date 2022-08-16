package pl.dabrowski.electrotools.wire.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.wire.Wire;
import pl.dabrowski.electrotools.wire.repository.WireRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadWireService {
  private final WireRepository wireRepository;

  public List<ReadWireDto> findAll() {
    return wireRepository.findAll().stream().map(Wire::toDto).toList();
  }

  public Page<ReadWireDto> pageAll(Pageable pageable) {
    return wireRepository.findAll(pageable).map(Wire::toDto);
  }

  public ReadWireDto findById(UUID wireId) {
    return wireRepository.findById(wireId).map(Wire::toDto).orElseThrow(() -> new NoSuchElementException("No Wire with id: " + wireId + ""));
  }
}
