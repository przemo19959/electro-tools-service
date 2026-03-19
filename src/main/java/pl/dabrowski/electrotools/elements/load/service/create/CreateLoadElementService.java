package pl.dabrowski.electrotools.elements.load.service.create;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.load.LoadElement;
import pl.dabrowski.electrotools.elements.load.repository.LoadElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateLoadElementService {
    private final LoadElementRepository loadElementRepository;
    private final ProjectRepository projectRepository;

    public LoadElement create(CreateLoadElementDto dto) {
        if (!projectRepository.existsById(dto.getProjectId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No project with id: " + dto.getProjectId());
        }

        Project project = projectRepository.getReferenceById(dto.getProjectId());

        return loadElementRepository.save(LoadElement.create(dto, project));
    }
}
