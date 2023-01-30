package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.service.create.CreateBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementService;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;

import java.util.Collections;
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
  @PreAuthorize("hasAuthority('read_elements')")
  public ResponseEntity<List<ReadAbstractElementDto>> getTree(@RequestParam UUID projectId) {
    return ResponseEntity.ok(readBasicElementService.getTree(projectId));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateLoadElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createBasicElementService.create(dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/{basicElementId}")
  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID basicElementId, @RequestBody UpdateLoadElementDto dto) {
    return ResponseEntity.ok(updateBasicElementService.update(basicElementId, dto).toDto(Collections.emptyList()));
  }

  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('edit_elements')")
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdIn(ids);
  }
}
