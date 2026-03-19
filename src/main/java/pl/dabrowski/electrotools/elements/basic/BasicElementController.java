package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.service.create.CreateBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementService;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BasicElementController.BASE_URL)
@RequiredArgsConstructor
public class BasicElementController {
  public static final String BASE_URL = "/api/v1/elements";

  private final ReadBasicElementService readBasicElementService;
  private final CreateBasicElementService createBasicElementService;
  private final UpdateBasicElementService updateBasicElementService;
  private final DeleteBasicElementService deleteBasicElementService;

  @GetMapping("/tree")
//  @PreAuthorize("hasAuthority('read_elements')")
  public ResponseEntity<List<ReadAbstractElementDto>> getTrees(@RequestParam UUID projectId) {
    return ResponseEntity.ok(readBasicElementService.getTrees(projectId));
  }

  @PostMapping
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateAbstractElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createBasicElementService.create(dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/{basicElementId}")
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID basicElementId, @RequestBody UpdateAbstractElementDto dto) {
    return ResponseEntity.ok(updateBasicElementService.update(basicElementId, dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/positions")
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<Void> updatePositions(@RequestBody List<UpdateBasicElementPositionDto> changes) {
    updateBasicElementService.updatePositions(changes);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/delete")
//  @PreAuthorize("hasAuthority('edit_elements')")
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdIn(ids);
  }
}
