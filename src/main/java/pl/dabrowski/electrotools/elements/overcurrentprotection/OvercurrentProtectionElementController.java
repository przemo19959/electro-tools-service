package pl.dabrowski.electrotools.elements.overcurrentprotection;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/overcurrentProtectionElements")
@RequiredArgsConstructor
public class OvercurrentProtectionElementController {
  private final ReadOvercurrentProtectionElementService readOvercurrentProtectionElementService;
  private final CreateOvercurrentProtectionElementService createOvercurrentProtectionElementService;
  private final UpdateOvercurrentProtectionElementService updateOvercurrentProtectionElementService;
  private final DeleteOvercurrentProtectionElementService deleteOvercurrentProtectionElementService;

  @GetMapping
  public ResponseEntity<List<ReadOvercurrentProtectionElementDto>> findAll(@RequestParam UUID projectId) {
    return ResponseEntity.ok(readOvercurrentProtectionElementService.findAll(projectId));
  }

  @GetMapping("/page")
  public ResponseEntity<Page<ReadOvercurrentProtectionElementDto>> pageAll(Pageable pageable) {
    return ResponseEntity.ok(readOvercurrentProtectionElementService.pageAll(pageable));
  }

  @GetMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<ReadOvercurrentProtectionElementDto> findById(@PathVariable UUID overcurrentProtectionElementId) {
    return ResponseEntity.ok(readOvercurrentProtectionElementService.findById(overcurrentProtectionElementId));
  }

  @PostMapping
  public ResponseEntity<ReadOvercurrentProtectionElementDto> create(@RequestBody CreateOvercurrentProtectionElementDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(createOvercurrentProtectionElementService.create(dto).toDto());
  }

  @PutMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<ReadOvercurrentProtectionElementDto> update(@PathVariable UUID overcurrentProtectionElementId, @RequestBody UpdateOvercurrentProtectionElementDto dto) {
    return ResponseEntity.ok(updateOvercurrentProtectionElementService.update(overcurrentProtectionElementId, dto).toDto());
  }

  @DeleteMapping("/{overcurrentProtectionElementId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID overcurrentProtectionElementId) {
    deleteOvercurrentProtectionElementService.deleteById(overcurrentProtectionElementId);
    return ResponseEntity.ok().build();
  }
}
