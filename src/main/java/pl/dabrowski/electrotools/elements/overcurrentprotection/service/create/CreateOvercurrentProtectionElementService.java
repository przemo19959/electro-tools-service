package pl.dabrowski.electrotools.elements.overcurrentprotection.service.create;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.repository.OvercurrentProtectionElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOvercurrentProtectionElementService {
    private final OvercurrentProtectionElementRepository overcurrentProtectionElementRepository;
    private final ProjectRepository projectRepository;

    public OvercurrentProtectionElement create(CreateOvercurrentProtectionElementDto dto) {
        if (!projectRepository.existsById(dto.getProjectId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No project with id: " + dto.getProjectId());
        }

        Project project = projectRepository.getReferenceById(dto.getProjectId());

        return overcurrentProtectionElementRepository.save(OvercurrentProtectionElement.create(dto, project));
    }
}
