package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.elements.load.LoadElementController;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class LoadElementApi {
    private final RequestSpecification spec;

    public Response create(CreateLoadElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(LoadElementController.BASE_URL);
    }

    public Response update(String loadElementId, UpdateLoadElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(LoadElementController.BASE_URL + "/{loadElementId}", loadElementId);
    }

    public Response deleteById(String loadElementId) {
        return given(spec)
                .delete(LoadElementController.BASE_URL + "/{loadElementId}", loadElementId);
    }
}

