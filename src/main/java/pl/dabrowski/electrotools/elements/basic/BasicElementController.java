package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/elements")
@RequiredArgsConstructor
public class BasicElementController {
  private final DeleteBasicElementService deleteBasicElementService;

  @GetMapping
  public ResponseEntity<List<ReadLoadElementDto>> findAll(@RequestParam UUID projectId) {
    return ResponseEntity.ok(Collections.emptyList());
  }

  @PostMapping
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdsIn(ids);
  }
}
