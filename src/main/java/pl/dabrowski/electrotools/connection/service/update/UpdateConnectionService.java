package pl.dabrowski.electrotools.connection.service.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;
import pl.dabrowski.electrotools.wire.Wire;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateConnectionService {
  private final ConnectionRepository connectionRepository;
  private final UpdateWireService updateWireService;

  public List<Connection> update(List<UpdateConnectionDto> dtos) {
    Map<UUID, UpdateConnectionDto> idDtoMap = dtos.stream()
        .collect(Collectors.toMap(UpdateConnectionDto::getId, Function.identity()));
    List<Connection> connections = connectionRepository.findAllById(idDtoMap.keySet());

    if (connections.size() != dtos.size()) {
      throw new NoSuchElementException("Some connections are not present in db, unable to update!");
    }

    return connectionRepository.saveAll(connections.stream()
        .map(v -> {
          UpdateConnectionDto dto = idDtoMap.get(v.getId());
          Wire wire = updateWireService.update(dto.getWire());
          return v.update(dto, wire);
        }).toList());
  }
}
