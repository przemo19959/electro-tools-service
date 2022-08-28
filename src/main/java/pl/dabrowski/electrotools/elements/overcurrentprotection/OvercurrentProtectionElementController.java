package pl.dabrowski.electrotools.elements.overcurrentprotection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.delete.DeleteOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementService;

import java.util.UUID;

@RestController
@RequestMapping("/overcurrentProtectionElements")
@RequiredArgsConstructor
public class OvercurrentProtectionElementController {
  private final ReadOvercurrentProtectionElementService readOvercurrentProtectionElementService;
  private final CreateOvercurrentProtectionElementService createOvercurrentProtectionElementService;
  private final UpdateOvercurrentProtectionElementService updateOvercurrentProtectionElementService;
  private final DeleteOvercurrentProtectionElementService deleteOvercurrentProtectionElementService;

  @PostMapping
  public ResponseEntity<ReadOvercurrentProtectionElementDto> create(@RequestBody CreateOvercurrentProtectionElementDto dto) {
    createOvercurrentProtectionElementService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<ReadOvercurrentProtectionElementDto> update(@PathVariable UUID overcurrentProtectionElementId, @RequestBody UpdateOvercurrentProtectionElementDto dto) {
    updateOvercurrentProtectionElementService.update(overcurrentProtectionElementId, dto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID overcurrentProtectionElementId) {
    deleteOvercurrentProtectionElementService.deleteById(overcurrentProtectionElementId);
    return ResponseEntity.ok().build();
  }
}
