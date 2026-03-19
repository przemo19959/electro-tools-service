package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.basic.BasicElementController;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementPositionDto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class BasicElementApi {
    private final RequestSpecification spec;

    public Response getTrees(UUID projectId) {
        return given(spec)
                .queryParam("projectId", projectId)
                .get(BasicElementController.BASE_URL + "/tree");
    }

    public Response create(CreateAbstractElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(BasicElementController.BASE_URL);
    }

    public Response update(String basicElementId, UpdateAbstractElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(BasicElementController.BASE_URL + "/{basicElementId}", basicElementId);
    }

    public Response updatePositions(List<UpdateBasicElementPositionDto> changes) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(changes)
                .put(BasicElementController.BASE_URL + "/positions");
    }

    public Response remove(String... ids) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(Arrays.stream(ids).map(UUID::fromString).toList())
                .post(BasicElementController.BASE_URL + "/delete");
    }
}

