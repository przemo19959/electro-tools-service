package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.basic.service.create.CreateBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementDto;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementService;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/elements")
@RequiredArgsConstructor
public class BasicElementController {
  private final ReadBasicElementService readBasicElementService;
  private final CreateBasicElementService createBasicElementService;
  private final UpdateBasicElementService updateBasicElementService;
  private final DeleteBasicElementService deleteBasicElementService;

  @GetMapping
  public ResponseEntity<List<ReadBasicElementDto>> findAll(@RequestParam UUID projectId) {
    return ResponseEntity.ok(readBasicElementService.findAll(projectId));
  }

  @PostMapping
  public ResponseEntity<ReadBasicElementDto> create(@RequestBody CreateLoadElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createBasicElementService.create(dto).toDto());
  }

  @PutMapping("/{basicElementId}")
  public ResponseEntity<ReadBasicElementDto> update(@PathVariable UUID basicElementId, @RequestBody UpdateLoadElementDto dto) {
    return ResponseEntity.ok(updateBasicElementService.update(basicElementId, dto).toDto());
  }

  @PostMapping("/delete")
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdsIn(ids);
  }
}
