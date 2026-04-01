package pl.dabrowski.electrotools.filter.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jooq.Field;

import static pl.dabrowski.electrotools.jooq.tables.TProjects.T_PROJECTS;

@RequiredArgsConstructor
@Getter
public enum ProjectFilterableColumn implements FilterableColumn {
    NAME(T_PROJECTS.NAME, FilterableColumnType.STRING),
    CREATED_BY(T_PROJECTS.CREATED_BY, FilterableColumnType.STRING),
    CREATED_DATE(T_PROJECTS.CREATED_DATE, FilterableColumnType.DATE),
    MODIFIED_BY(T_PROJECTS.MODIFIED_BY, FilterableColumnType.STRING),
    MODIFIED_DATE(T_PROJECTS.MODIFIED_DATE, FilterableColumnType.DATE);

    private final Field<?> field;
    private final FilterableColumnType type;
}
