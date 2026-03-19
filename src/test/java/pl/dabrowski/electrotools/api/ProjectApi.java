package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.project.ProjectController;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class ProjectApi {
    private final RequestSpecification spec;

    public Response findAll() {
        return given(spec)
                .get(ProjectController.BASE_URL);
    }

    public Response pageAll(int page, int size) {
        return given(spec)
                .queryParam("page", page)
                .queryParam("size", size)
                .get(ProjectController.BASE_URL + "/page");
    }

    public Response findById(String projectId) {
        return given(spec)
                .get(ProjectController.BASE_URL + "/{projectId}", projectId);
    }

    public Response create(CreateProjectDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(ProjectController.BASE_URL);
    }

    public Response update(String projectId, UpdateProjectDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(ProjectController.BASE_URL + "/{projectId}", projectId);
    }

    public Response deleteAllById(List<UUID> projectIds) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(projectIds)
                .delete(ProjectController.BASE_URL);
    }
}
