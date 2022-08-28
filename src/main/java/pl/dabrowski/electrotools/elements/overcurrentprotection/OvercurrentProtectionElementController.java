package pl.dabrowski.electrotools.elements.overcurrentprotection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete.DeleteOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementService;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/overcurrentProtectionElements")
@RequiredArgsConstructor
public class OvercurrentProtectionElementController {
  private final CreateOvercurrentProtectionElementService createOvercurrentProtectionElementService;
  private final UpdateOvercurrentProtectionElementService updateOvercurrentProtectionElementService;
  private final DeleteOvercurrentProtectionElementService deleteOvercurrentProtectionElementService;

  @PostMapping
  public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateOvercurrentProtectionElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createOvercurrentProtectionElementService.create(dto).toDto(Collections.emptyList()));
  }

  @PutMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID overcurrentProtectionElementId, @RequestBody UpdateOvercurrentProtectionElementDto dto) {
    return ResponseEntity.ok(updateOvercurrentProtectionElementService.update(overcurrentProtectionElementId, dto).toDto(Collections.emptyList()));
  }

  @DeleteMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID overcurrentProtectionElementId) {
    deleteOvercurrentProtectionElementService.deleteById(overcurrentProtectionElementId);
    return ResponseEntity.ok().build();
  }
}
