package pl.dabrowski.electrotools.project.service.exists;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExistProjectService {
  public static final String ERROR_MESSAGE = "PROJECT_ALREADY_EXISTS";

  private final ProjectRepository projectRepository;

  public void check(String name) {
    check(null, name);
  }

  public void check(UUID id, String name) {
    boolean exists = id != null
        ? projectRepository.existsByIdNotAndName(id, name)
        : projectRepository.existsByName(name);

    if (exists) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, ERROR_MESSAGE);
    }
  }
}
