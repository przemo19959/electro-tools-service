package pl.dabrowski.electrotools.connection.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.connection.Connection;
import pl.dabrowski.electrotools.connection.repository.ConnectionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadConnectionService {
    private final ConnectionRepository connectionRepository;

    public List<ReadConnectionDto> findAll(List<String> elementIds) {
        return connectionRepository.findAllByElementIdsIn(elementIds)
            .stream()
            .map(Connection::toDto)
            .toList();
    }

//    public Page<ReadConnectionDto> pageAll(Pageable pageable) {
//        return connectionRepository.findAll(pageable).map(Connection::toDto);
//    }
//
//    public ReadConnectionDto findById(UUID connectionId) {
//        return connectionRepository.findById(connectionId).map(Connection::toDto).orElseThrow(() -> new NoSuchElementException("No Connection with id: " + connectionId + ""));
//    }
}
