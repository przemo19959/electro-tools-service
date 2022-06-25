package pl.dabrowski.electrotools.project.service.read;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadProjectService {

    private final ProjectRepository projectRepository;

    public List<ReadProjectDto> findAll() {
        return projectRepository.findAll().stream().map(Project::toDto).collect(Collectors.toList());
    }

    public Page<ReadProjectDto> pageAll(Pageable pageable) {
        return projectRepository.findAll(pageable).map(Project::toDto);
    }

    public ReadProjectDto findById(UUID projectId) {
        return projectRepository.findById(projectId).map(Project::toDto).orElseThrow(() -> new NoSuchElementException("No Project with id: " + projectId + ""));
    }
}
