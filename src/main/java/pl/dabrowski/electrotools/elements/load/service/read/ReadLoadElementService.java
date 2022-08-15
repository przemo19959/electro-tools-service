package pl.dabrowski.electrotools.elements.load.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadLoadElementService {

    private final LoadElementRepository loadElementRepository;

    public List<ReadLoadElementDto> findAll() {
        return loadElementRepository.findAll().stream().map(LoadElement::toDto).toList();
    }

    public Page<ReadLoadElementDto> pageAll(Pageable pageable) {
        return loadElementRepository.findAll(pageable).map(LoadElement::toDto);
    }

    public ReadLoadElementDto findById(UUID loadElementId) {
        return loadElementRepository.findById(loadElementId).map(LoadElement::toDto).orElseThrow(() -> new NoSuchElementException("No LoadElement with id: " + loadElementId + ""));
    }
}
