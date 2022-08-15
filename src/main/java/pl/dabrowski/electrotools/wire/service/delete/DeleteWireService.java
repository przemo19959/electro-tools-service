package pl.dabrowski.electrotools.wire.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.wire.repository.WireRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteWireService {

  private final WireRepository wireRepository;

  public void deleteById(UUID wireId) {
    wireRepository.deleteById(wireId);
  }
}
