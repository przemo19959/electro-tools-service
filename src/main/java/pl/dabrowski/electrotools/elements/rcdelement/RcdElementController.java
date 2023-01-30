package pl.dabrowski.electrotools.elements.rcdelement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.create.CreateRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.create.CreateRcdElementService;
import pl.dabrowski.electrotools.elements.rcdelement.service.delete.DeleteRcdElementService;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementService;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/rcdElements")
@RequiredArgsConstructor
public class RcdElementController {
  private final CreateRcdElementService createRcdElementService;
  private final UpdateRcdElementService updateRcdElementService;
  private final DeleteRcdElementService deleteRcdElementService;

  @PostMapping
  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateRcdElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createRcdElementService.create(dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/{rcdElementId}")
  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID rcdElementId, @RequestBody UpdateRcdElementDto dto) {
    return ResponseEntity.ok(updateRcdElementService.update(rcdElementId, dto).toDto(Collections.emptyList()));
  }

  @DeleteMapping("/{rcdElementId}")
  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<Void> deleteById(@PathVariable UUID rcdElementId) {
    deleteRcdElementService.deleteById(rcdElementId);
    return ResponseEntity.ok().build();
  }
}
