package pl.dabrowski.electrotools.project.service.read;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.elements.basic.QBasicElement;
import pl.dabrowski.electrotools.elements.load.QLoadElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.QOvercurrentProtectionElement;
import pl.dabrowski.electrotools.elements.rcdelement.QRcdElement;
import pl.dabrowski.electrotools.elements.terminalelement.QTerminalElement;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.QProject;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadProjectService {
  private final ProjectRepository projectRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public List<ReadProjectDto> findAll() {
    return projectRepository.findAll()
        .stream()
        .map(Project::toDto)
        .toList();
  }

  public Page<ReadProjectDto> pageAll(Pageable pageable,
                                      Optional<String> query) {
    QProject project = QProject.project;
    QBasicElement basicElement = QBasicElement.basicElement;
    QLoadElement loadElement = QLoadElement.loadElement;
    QOvercurrentProtectionElement overcurrentProtectionElement = QOvercurrentProtectionElement.overcurrentProtectionElement;
    QTerminalElement terminalElement = QTerminalElement.terminalElement;
    QRcdElement rcdElement = QRcdElement.rcdElement;

    BooleanBuilder bb = new BooleanBuilder();
    query.ifPresent(v -> bb.andAnyOf(
        project.name.containsIgnoreCase(v),
        project.createdBy.containsIgnoreCase(v)
    ));


    NumberExpression<Long> bCount = Expressions.asNumber(JPAExpressions.select(basicElement.count()).from(basicElement).where(basicElement.project.id.eq(project.id)));
    NumberExpression<Long> lCount = Expressions.asNumber(JPAExpressions.select(loadElement.count()).from(loadElement).where(loadElement.project.id.eq(project.id)));
    NumberExpression<Long> oCount = Expressions.asNumber(JPAExpressions.select(overcurrentProtectionElement.count()).from(overcurrentProtectionElement).where(overcurrentProtectionElement.project.id.eq(project.id)));
    NumberExpression<Long> tCount = Expressions.asNumber(JPAExpressions.select(terminalElement.count()).from(terminalElement).where(terminalElement.project.id.eq(project.id)));
    NumberExpression<Long> rCount = Expressions.asNumber(JPAExpressions.select(rcdElement.count()).from(rcdElement).where(rcdElement.project.id.eq(project.id)));
    JPAQuery<Tuple> sql = jpaQueryFactory.select(
        project.id, 
        project.name, 
        project.createdBy,
        project.modifiedBy,
        project.modifiedDate, 
        bCount, 
        lCount, 
        oCount, 
        tCount, 
        rCount
    )
        .from(project)
        .where(bb)
        .orderBy(project.modifiedDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    return new PageImpl<>(sql.fetch().stream()
        .map(v -> new ReadProjectDto(
            v.get(project.id), 
            v.get(project.name), 
            v.get(project.createdBy),
            v.get(project.modifiedBy),
            v.get(project.modifiedDate),
            v.get(5, Long.class) + v.get(6, Long.class) + v.get(7, Long.class) + v.get(8, Long.class) + v.get(9, Long.class)))
        .toList(), pageable, sql.fetchCount());
  }

  public ReadProjectDto findById(UUID projectId) {
    return projectRepository
        .findById(projectId)
        .map(Project::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Project with id: " + projectId + ""));
  }
}
