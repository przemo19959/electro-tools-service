package pl.dabrowski.electrotools.project;

import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import pl.dabrowski.electrotools.filter.column.ProjectFilterableColumn;
import pl.dabrowski.electrotools.filter.operator.FilterColumnOperator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProjectTools {
    public static final FunctionDeclaration PROJECT_FIND_ALL_TOOL = FunctionDeclaration.builder()
            .name("findAll")
            .description("""
                        Find all projects. Returns a list of projects without pagination. Use with caution for large datasets.
                    
                        Use this function whenever you need to know which projects already exist,
                        verify uniqueness, or inspect project names before creating new projects.
                    """)
            .build();
    public static final FunctionDeclaration PROJECT_PAGE_ALL_TOOL = FunctionDeclaration.builder()
            .name("pageAll")
            .description("""
                        Page all projects with optional query and filter.
                        Returns a paginated list of projects matching the given criteria.
                        Page and size controls returned page.
                        Query is applied to all columns with OR operator. For specific column search use filters parameter.
                        Filters parameter allows to create complex filter criteria with nested groups and AND/OR operators.
                    """)
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "page",
                            Schema.builder()
                                    .type(Type.Known.INTEGER)
                                    .description("Page number starting from 0")
                                    .build(),
                            "size",
                            Schema.builder()
                                    .type(Type.Known.INTEGER)
                                    .description("Number of results per page")
                                    .build(),
                            "query",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Search text applied to all columns. That value is applied to all columns with OR operator. For specific column search use filter parameter.")
                                    .build(),
                            "filters",
                            Schema.builder()
                                    .type(Type.Known.ARRAY)
                                    .items(Schema.builder()
                                            .type(Type.Known.OBJECT)
                                            .description("""
                                                        Filter group with nested filters.
                                                        Allows to create complex filter criteria.
                                                        Those conditions will be always applied together i.e. with AND operator.
                                                    """)
                                            .properties(Map.of(
                                                    "operator",
                                                    Schema.builder()
                                                            .type(Type.Known.STRING)
                                                            .description("Column operator to apply. Defines how column is filtered. Always required.")
                                                            .format("enum")
                                                            .enum_(Arrays.stream(FilterColumnOperator.values()).map(FilterColumnOperator::name).toList())
                                                            .build(),
                                                    "column",
                                                    Schema.builder()
                                                            .type(Type.Known.STRING)
                                                            .description("Column name to filter by. Always required.")
                                                            .format("enum")
                                                            .enum_(Arrays.stream(ProjectFilterableColumn.values()).map(ProjectFilterableColumn::name).toList())
                                                            .build(),
                                                    "value",
                                                    Schema.builder()
                                                            .type(Type.Known.STRING)
                                                            .description("Value to filter by. Most of the time it's required. Otherwise empty string is used.")
                                                            .build()
                                            )).build())
                                    .build()))
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_FIND_BY_ID_TOOL = FunctionDeclaration.builder()
            .name("findById")
            .description("Find project by id. Returns a single project matching the given id.")
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "id",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Project id as UUID string")
                                    .build()
                    ))
                    .required(List.of("id"))
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_FIND_DISTINCT_VALUES_TOOL = FunctionDeclaration.builder()
            .name("findDistinctValues")
            .description("Find distinct values for a given column")
            .parameters(Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Column name to find distinct values for")
                    .format("enum")
                    .enum_(Arrays.stream(ProjectFilterableColumn.values()).map(ProjectFilterableColumn::name).toList())
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_CREATE_TOOL = FunctionDeclaration.builder()
            .name("create")
            .description("""
                    Create a new project.
                    
                    Call this function once for each project that must be created.
                    The function may be invoked multiple times in the same conversation.
                    """)
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "name",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Name of the project that will be created")
                                    .build()
                    ))
                    .required(List.of("name"))
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_UPDATE_TOOL = FunctionDeclaration.builder()
            .name("update")
            .description("""
                        Update an existing project.
                    
                        Call this function once for each project that must be updated.
                        The function may be invoked multiple times in the same conversation.
                        Used to rename a project. Currently only name can be updated, but in the future more fields can be added.
                    """)
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "id",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Project id as UUID string. It determines which project will be updated")
                                    .build(),
                            "name",
                            Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("New name of the project. It determines how the project will be updated")
                                    .build()
                    ))
                    .required(List.of("id", "name"))
                    .build())
            .build();
    public static final FunctionDeclaration PROJECT_DELETE_ALL_BY_ID_TOOL = FunctionDeclaration.builder()
            .name("deleteAllById")
            .description("""
                    Delete projects by a list of ids.
                    Use this function to delete one or more projects.
                    Function handles multiple ids, so it should be used once whenever possible.
                    """)
            .parameters(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "ids",
                            Schema.builder()
                                    .type(Type.Known.ARRAY)
                                    .items(Schema.builder()
                                            .type(Type.Known.STRING)
                                            .description("Project id as UUID string")
                                            .build())
                                    .build()
                    ))
                    .required(List.of("ids"))
                    .build())
            .build();
}
