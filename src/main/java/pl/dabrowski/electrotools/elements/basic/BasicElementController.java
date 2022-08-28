package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.service.create.CreateBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
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

  @GetMapping("/tree")
  public ResponseEntity<List<ReadAbstractElementDto>> getTree(@RequestParam UUID projectId) {
    return ResponseEntity.ok(readBasicElementService.getTree(projectId));
  }

  @PostMapping
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateLoadElementDto dto) {
    createBasicElementService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{basicElementId}")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID basicElementId, @RequestBody UpdateLoadElementDto dto) {
    updateBasicElementService.update(basicElementId, dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/delete")
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdIn(ids);
  }

  @PostMapping("/deep-delete")
  public void deepRemove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdInWithConnections(ids);
  }
}
