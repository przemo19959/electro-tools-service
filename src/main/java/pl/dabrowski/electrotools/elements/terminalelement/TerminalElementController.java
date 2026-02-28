package pl.dabrowski.electrotools.elements.terminalelement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.create.CreateTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.create.CreateTerminalElementService;
import pl.dabrowski.electrotools.elements.terminalelement.service.delete.DeleteTerminalElementService;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementService;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/terminalElements")
@RequiredArgsConstructor
public class TerminalElementController {
  private final CreateTerminalElementService createTerminalElementService;
  private final UpdateTerminalElementService updateTerminalElementService;
  private final DeleteTerminalElementService deleteTerminalElementService;

  @PostMapping
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateTerminalElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createTerminalElementService.create(dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/{terminalElementId}")
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID terminalElementId, @RequestBody UpdateTerminalElementDto dto) {
    return ResponseEntity.ok(updateTerminalElementService.update(terminalElementId, dto).toDto(Collections.emptyList()));
  }

  @DeleteMapping("/{terminalElementId}")
//  @PreAuthorize("hasAuthority('edit_elements')")
  public ResponseEntity<Void> deleteById(@PathVariable UUID terminalElementId) {
    deleteTerminalElementService.deleteById(terminalElementId);
    return ResponseEntity.ok().build();
  }
}
