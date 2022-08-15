package pl.dabrowski.electrotools.connection.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;
import pl.dabrowski.electrotools.wire.Wire;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateConnectionService {

  private final ConnectionRepository connectionRepository;

  public Connection update(UUID connectionId, UpdateConnectionDto dto) {
    return connectionRepository.findById(connectionId)
        .map(v -> v.update(dto, new Wire()))
        .map(connectionRepository::save)
        .orElseThrow(() -> new NoSuchElementException("No Connection with id: " + connectionId + ""));
  }
}
