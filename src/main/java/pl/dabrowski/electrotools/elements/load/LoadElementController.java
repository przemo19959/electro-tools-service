package pl.dabrowski.electrotools.elements.load;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementService;

import java.util.UUID;

@RestController
@RequestMapping("/loadElements")
@RequiredArgsConstructor
public class LoadElementController {
    private final CreateLoadElementService createLoadElementService;
    private final UpdateLoadElementService updateLoadElementService;
    private final DeleteLoadElementService deleteLoadElementService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateLoadElementDto dto) {
      createLoadElementService.create(dto);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{loadElementId}")
    public ResponseEntity<ReadLoadElementDto> update(@PathVariable UUID loadElementId, @RequestBody UpdateLoadElementDto dto) {
      updateLoadElementService.update(loadElementId, dto);
      return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{loadElementId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID loadElementId) {
        deleteLoadElementService.deleteById(loadElementId);
        return ResponseEntity.ok().build();
    }
}
