package pl.dabrowski.electrotools.elements.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.electrotools.elements.basic.service.delete.DeleteBasicElementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/elements")
@RequiredArgsConstructor
public class BasicElementController {
  private final DeleteBasicElementService deleteBasicElementService;

  @PostMapping
  public void remove(@RequestBody List<UUID> ids) {
    deleteBasicElementService.deleteAllByIdsIn(ids);
  }
}
