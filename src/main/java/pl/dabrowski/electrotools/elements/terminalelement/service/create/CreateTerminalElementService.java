package pl.dabrowski.electrotools.elements.terminalelement.service.create;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTerminalElementService {
    private final TerminalElementRepository terminalElementRepository;
    private final ProjectRepository projectRepository;

    public TerminalElement create(CreateTerminalElementDto dto) {
        if (dto.getParentId() != null || dto.getWire() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal element must not have parents");
        }

        if (!projectRepository.existsById(dto.getProjectId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No project with id: " + dto.getProjectId());
        }

        Project project = projectRepository.getReferenceById(dto.getProjectId());
        return terminalElementRepository.save(TerminalElement.create(dto, project));
    }
}
