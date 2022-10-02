package pl.dabrowski.electrotools.project.service.read;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.QProject;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadProjectService {
  private final ProjectRepository projectRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public List<ReadProjectDto> findAll() {
    return projectRepository.findAll().stream().map(Project::toDto).toList();
  }

  public Page<ReadProjectDto> pageAll(Pageable pageable,
                                      Optional<String> query) {
    QProject project = QProject.project;

    BooleanBuilder bb = new BooleanBuilder();
    query.ifPresent(v -> bb.andAnyOf(
        project.name.containsIgnoreCase(v),
        project.owner.containsIgnoreCase(v)
    ));

    JPAQuery<Project> sql = jpaQueryFactory.selectFrom(project)
        .where(bb)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    return new PageImpl<>(sql.fetch().stream()
        .map(Project::toDto)
        .toList(), pageable, sql.fetchCount());
  }

  public ReadProjectDto findById(UUID projectId) {
    return projectRepository.findById(projectId).map(Project::toDto).orElseThrow(() -> new NoSuchElementException("No Project with id: " + projectId + ""));
  }
}
