package pl.dabrowski.electrotools.elements.rcdelement.service.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.elements.rcdelement.repository.RcdElementRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteRcdElementService {
    private final RcdElementRepository rcdElementRepository;

    public void deleteById(UUID rcdElementId) {
        rcdElementRepository.deleteById(rcdElementId);
    }

    public void deleteAllByIdIn(List<UUID> ids) {
        rcdElementRepository.deleteAll(rcdElementRepository.findAllById(ids));
    }
}
