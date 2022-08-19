package pl.dabrowski.electrotools.connection.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteConnectionService {
  private final ConnectionRepository connectionRepository;

  public void deleteAllById(List<UUID> connectionIds) {
    connectionRepository.deleteAllById(connectionIds);
  }
}
