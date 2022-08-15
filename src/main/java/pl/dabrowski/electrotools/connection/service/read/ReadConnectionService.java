package pl.dabrowski.electrotools.connection.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadConnectionService {

    private final ConnectionRepository connectionRepository;

    public List<ReadConnectionDto> findAll() {
        return connectionRepository.findAll().stream().map(Connection::toDto).toList();
    }

    public Page<ReadConnectionDto> pageAll(Pageable pageable) {
        return connectionRepository.findAll(pageable).map(Connection::toDto);
    }

    public ReadConnectionDto findById(UUID connectionId) {
        return connectionRepository.findById(connectionId).map(Connection::toDto).orElseThrow(() -> new NoSuchElementException("No Connection with id: " + connectionId + ""));
    }
}
