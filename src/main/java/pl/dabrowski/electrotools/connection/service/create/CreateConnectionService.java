package pl.dabrowski.electrotools.connection.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;
import pl.dabrowski.electrotools.wire.Wire;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateConnectionService {
  private final ConnectionRepository connectionRepository;

  public Connection create(CreateConnectionDto dto) {

    return connectionRepository.save(Connection.create(dto, new Wire()));
  }
}
