package pl.dabrowski.electrotools.elements.basic.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.abstractelement.AbstractElement;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.repository.BasicElementRepository;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementService;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementService;
import pl.dabrowski.electrotools.elements.terminalelement.service.read.ReadTerminalElementService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadBasicElementService {
  private final BasicElementRepository basicElementRepository;
  private final ReadLoadElementService readLoadElementService;
  private final ReadOvercurrentProtectionElementService readOvercurrentProtectionElementService;
  private final ReadTerminalElementService readTerminalElementService;

  public List<ReadAbstractElementDto> getTree(UUID projectId) {
    List<AbstractElement> elements = new ArrayList<>();

    elements.addAll(basicElementRepository.findAllByProjectId(projectId));
    elements.addAll(readLoadElementService.findAll(projectId));
    elements.addAll(readOvercurrentProtectionElementService.findAll(projectId));
    elements.addAll(readTerminalElementService.findAll(projectId));

    Map<Optional<UUID>, List<AbstractElement>> groupedByParent = elements.stream()
        .collect(Collectors.groupingBy(v -> Optional.ofNullable(v.getParentId())));

    List<ReadAbstractElementDto> result = new ArrayList<>();
    if (groupedByParent.containsKey(Optional.empty())) {
      groupedByParent.get(Optional.empty()).forEach(v -> result.add(treeChildren(v, groupedByParent)));
    }

    return result;
  }

  private ReadAbstractElementDto treeChildren(AbstractElement parent,
                                              Map<Optional<UUID>, List<AbstractElement>> childrenByParent) {
    List<AbstractElement> children = childrenByParent.get(Optional.ofNullable(parent.getId()));
    if (children == null) {
      return parent.toDto(Collections.emptyList());
    } else {
      return parent.toDto(children.stream().map(v -> treeChildren(v, childrenByParent)).toList());
    }
  }
}
