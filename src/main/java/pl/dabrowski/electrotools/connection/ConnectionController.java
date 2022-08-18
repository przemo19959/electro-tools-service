package pl.dabrowski.electrotools.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.connection.service.create.CreateConnectionDto;
import pl.dabrowski.electrotools.connection.service.create.CreateConnectionService;
import pl.dabrowski.electrotools.connection.service.delete.DeleteConnectionService;
import pl.dabrowski.electrotools.connection.service.read.ReadConnectionDto;
import pl.dabrowski.electrotools.connection.service.read.ReadConnectionService;
import pl.dabrowski.electrotools.connection.service.update.UpdateConnectionDto;
import pl.dabrowski.electrotools.connection.service.update.UpdateConnectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {
  private final ReadConnectionService readConnectionService;
  private final CreateConnectionService createConnectionService;
  private final UpdateConnectionService updateConnectionService;
  private final DeleteConnectionService deleteConnectionService;

  @GetMapping
  public ResponseEntity<List<ReadConnectionDto>> findAll(@RequestBody List<String> elementIds) {
    return ResponseEntity.ok(readConnectionService.findAll(elementIds));
  }

//    @GetMapping("/page")
//    public ResponseEntity<Page<ReadConnectionDto>> pageAll(Pageable pageable) {
//        return ResponseEntity.ok(readConnectionService.pageAll(pageable));
//    }
//
//    @GetMapping("/{connectionId}")
//    public ResponseEntity<ReadConnectionDto> findById(@PathVariable UUID connectionId) {
//        return ResponseEntity.ok(readConnectionService.findById(connectionId));
//    }

  @PostMapping
  public ResponseEntity<List<ReadConnectionDto>> create(@RequestBody List<CreateConnectionDto> dtos) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createConnectionService.create(dtos)
        .stream()
        .map(Connection::toDto)
        .toList());
  }

  @PutMapping
  public ResponseEntity<List<ReadConnectionDto>> update(@RequestBody List<UpdateConnectionDto> dtos) {
    return ResponseEntity.ok(updateConnectionService.update(dtos).stream().map(Connection::toDto).toList());
  }

  @DeleteMapping("/{connectionId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID connectionId) {
    deleteConnectionService.deleteById(connectionId);
    return ResponseEntity.ok().build();
  }
}
