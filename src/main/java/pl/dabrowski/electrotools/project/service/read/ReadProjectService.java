package pl.dabrowski.electrotools.project.service.read;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.filter.FilterGroupDto;
import pl.dabrowski.electrotools.filter.column.ProjectFilterableColumn;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.project.repository.ProjectRepository;
import pl.dabrowski.electrotools.utils.JooqUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.dabrowski.electrotools.jooq.Tables.*;
import static pl.dabrowski.electrotools.utils.JooqUtils.query;
import static pl.dabrowski.electrotools.utils.JooqUtils.resolve;

@Service
@RequiredArgsConstructor
public class ReadProjectService {
    private final ProjectRepository projectRepository;
    private final DSLContext dslContext;

    public List<ReadProjectDto> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(Project::toDto)
                .toList();
    }

    public Page<ReadProjectDto> pageAll(Pageable pageable,
                                        Optional<String> query,
                                        FilterGroupDto filter
    ) {
        var conditions = Optional.ofNullable(filter).orElse(FilterGroupDto.empty()).process();
        conditions = conditions.and(query
                .map(v -> query(v, T_PROJECTS.NAME, T_PROJECTS.CREATED_BY, T_PROJECTS.MODIFIED_BY, JooqUtils.format(T_PROJECTS.MODIFIED_DATE)))
                .orElse(DSL.noCondition()));

        var base = dslContext.with("base").as(
                DSL.select(
                                DSL.count().over().as("totalCount"),
                                T_PROJECTS.ID,
                                T_PROJECTS.NAME,
                                T_PROJECTS.CREATED_BY,
                                T_PROJECTS.MODIFIED_BY,
                                T_PROJECTS.MODIFIED_DATE,
                                DSL.selectCount()
                                        .from(T_BASIC_ELEMENTS)
                                        .where(T_BASIC_ELEMENTS.PROJECT_ID.eq(T_PROJECTS.ID)).asField().as("basicCount"),
                                DSL.selectCount()
                                        .from(T_LOAD_ELEMENTS)
                                        .where(T_LOAD_ELEMENTS.PROJECT_ID.eq(T_PROJECTS.ID)).asField().as("loadCount"),
                                DSL.selectCount()
                                        .from(T_OVERCURRENT_PROTECTION_ELEMENTS)
                                        .where(T_OVERCURRENT_PROTECTION_ELEMENTS.PROJECT_ID.eq(T_PROJECTS.ID)).asField().as("overcurrentCount"),
                                DSL.selectCount()
                                        .from(T_TERMINAL_ELEMENTS)
                                        .where(T_TERMINAL_ELEMENTS.PROJECT_ID.eq(T_PROJECTS.ID)).asField().as("terminalCount"),
                                DSL.selectCount()
                                        .from(T_RCD_ELEMENTS)
                                        .where(T_RCD_ELEMENTS.PROJECT_ID.eq(T_PROJECTS.ID)).asField().as("rcdCount")
                        )
                        .from(T_PROJECTS)
                        .where(conditions)
                        .orderBy(T_PROJECTS.MODIFIED_DATE.desc())
        );

        var baseTable = DSL.table("base");
        var content = base.selectFrom(baseTable)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (content.isEmpty()) {
            var totalCount = dslContext.select(DSL.count())
                    .from(T_PROJECTS)
                    .where(conditions)
                    .fetchOne(0, Long.class);
            return new PageImpl<>(List.of(), pageable, Optional.ofNullable(totalCount).orElse(0L));
        }

        return new PageImpl<>(content.stream()
                .map(v -> new ReadProjectDto(
                        v.get(T_PROJECTS.ID),
                        v.get(T_PROJECTS.NAME),
                        v.get(T_PROJECTS.CREATED_BY),
                        v.get(T_PROJECTS.MODIFIED_BY),
                        toInstant(v.get(resolve("base", T_PROJECTS.MODIFIED_DATE), LocalDateTime.class)),
                        nonNull(v.get("basicCount", Long.class))
                                + nonNull(v.get("loadCount", Long.class))
                                + nonNull(v.get("overcurrentCount", Long.class))
                                + nonNull(v.get("terminalCount", Long.class))
                                + nonNull(v.get("rcdCount", Long.class))
                ))
                .toList(),
                pageable,
                content.getFirst().get("totalCount", Long.class)
        );
    }

    private static long nonNull(Long value) {
        return value == null ? 0L : value;
    }

    private static Instant toInstant(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    public ReadProjectDto findById(UUID projectId) {
        return projectRepository
                .findById(projectId)
                .map(Project::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Project with id: " + projectId + ""));
    }

    public List<String> findDistinctValues(String column) {
        var filterableColumn = ProjectFilterableColumn.fromName(column)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid column: " + column));

        return dslContext.selectDistinct(filterableColumn.getField())
                .from(T_PROJECTS)
                .fetchInto(String.class);
    }
}
