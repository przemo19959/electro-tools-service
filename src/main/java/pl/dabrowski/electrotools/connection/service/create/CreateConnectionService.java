package pl.dabrowski.electrotools.connection.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;
import pl.dabrowski.electrotools.wire.service.create.CreateWireService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateConnectionService {
  private final ConnectionRepository connectionRepository;
  private final CreateWireService createWireService;

  public List<Connection> create(List<CreateConnectionDto> dtos) {
    return connectionRepository.saveAll(dtos.stream()
        .map(v -> Connection.create(v, createWireService.create(v.getWire())))
        .toList());
  }
}
