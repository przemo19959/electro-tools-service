package pl.dabrowski.electrotools.wire;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;
import pl.dabrowski.electrotools.wire.service.create.CreateWireService;
import pl.dabrowski.electrotools.wire.service.delete.DeleteWireService;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;
import pl.dabrowski.electrotools.wire.service.read.ReadWireService;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wires")
@RequiredArgsConstructor
public class WireController {
  private final ReadWireService readWireService;
  private final CreateWireService createWireService;
  private final UpdateWireService updateWireService;
  private final DeleteWireService deleteWireService;

  @GetMapping
  public ResponseEntity<List<ReadWireDto>> findAll() {
    return ResponseEntity.ok(readWireService.findAll());
  }

  @GetMapping("/page")
  public ResponseEntity<Page<ReadWireDto>> pageAll(Pageable pageable) {
    return ResponseEntity.ok(readWireService.pageAll(pageable));
  }

  @GetMapping("/{wireId}")
  public ResponseEntity<ReadWireDto> findById(@PathVariable UUID wireId) {
    return ResponseEntity.ok(readWireService.findById(wireId));
  }

  @PostMapping
  public ResponseEntity<ReadWireDto> create(@RequestBody CreateWireDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createWireService.create(dto).toDto());
  }

  @PutMapping
  public ResponseEntity<ReadWireDto> update(@RequestBody UpdateWireDto dto) {
    return ResponseEntity.ok(updateWireService.update(dto).toDto());
  }

  @DeleteMapping("/{wireId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID wireId) {
    deleteWireService.deleteById(wireId);
    return ResponseEntity.ok().build();
  }
}
