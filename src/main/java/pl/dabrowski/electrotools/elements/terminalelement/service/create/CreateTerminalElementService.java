package pl.dabrowski.electrotools.elements.terminalelement.service.create;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;
import pl.dabrowski.electrotools.elements.terminalelement.repository.TerminalElementRepository;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import javax.transaction.Transactional;

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

        Project project = projectRepository.getReferenceById(dto.getProjectId());
        return terminalElementRepository.save(TerminalElement.create(dto, project));
    }
}
