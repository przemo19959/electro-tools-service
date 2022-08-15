package pl.dabrowski.electrotools.elements.load;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.delete.DeleteLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementService;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loadElements")
@RequiredArgsConstructor
public class LoadElementController {

    private final ReadLoadElementService readLoadElementService;

    private final CreateLoadElementService createLoadElementService;

    private final UpdateLoadElementService updateLoadElementService;

    private final DeleteLoadElementService deleteLoadElementService;

    @GetMapping
    public ResponseEntity<List<ReadLoadElementDto>> findAll() {
        return ResponseEntity.ok(readLoadElementService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ReadLoadElementDto>> pageAll(Pageable pageable) {
        return ResponseEntity.ok(readLoadElementService.pageAll(pageable));
    }

    @GetMapping("/{loadElementId}")
    public ResponseEntity<ReadLoadElementDto> findById(@PathVariable UUID loadElementId) {
        return ResponseEntity.ok(readLoadElementService.findById(loadElementId));
    }

    @PostMapping
    public ResponseEntity<ReadLoadElementDto> create(@RequestBody CreateLoadElementDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createLoadElementService.create(dto).toDto());
    }

    @PutMapping("/{loadElementId}")
    public ResponseEntity<ReadLoadElementDto> update(@PathVariable UUID loadElementId, @RequestBody UpdateLoadElementDto dto) {
        return ResponseEntity.ok(updateLoadElementService.update(loadElementId, dto).toDto());
    }

    @DeleteMapping("/{loadElementId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID loadElementId) {
        deleteLoadElementService.deleteById(loadElementId);
        return ResponseEntity.ok().build();
    }
}
