package pl.dabrowski.electrotools.wire.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.wire.Wire;
import pl.dabrowski.electrotools.wire.repository.WireRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateWireService {

  private final WireRepository wireRepository;

  public Wire create(CreateWireDto dto) {
    return wireRepository.save(Wire.create(dto));
  }
}
