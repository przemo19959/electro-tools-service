package pl.dabrowski.electrotools.elements.load;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementService;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/loadElements")
@RequiredArgsConstructor
public class LoadElementController {
    private final CreateLoadElementService createLoadElementService;
    private final UpdateLoadElementService updateLoadElementService;
    private final DeleteLoadElementService deleteLoadElementService;

    @PostMapping
    public ResponseEntity<ReadAbstractElementDto> create(@RequestBody CreateLoadElementDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createLoadElementService.create(dto).toDto(Collections.emptyList()));
    }

    @PutMapping("/{loadElementId}")
    public ResponseEntity<ReadAbstractElementDto> update(@PathVariable UUID loadElementId, @RequestBody UpdateLoadElementDto dto) {
        return ResponseEntity.ok(updateLoadElementService.update(loadElementId, dto).toDto(Collections.emptyList()));
    }

    @DeleteMapping("/{loadElementId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID loadElementId) {
        deleteLoadElementService.deleteById(loadElementId);
        return ResponseEntity.ok().build();
    }
}
