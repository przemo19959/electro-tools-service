package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElementController;
import pl.dabrowski.electrotools.elements.rcdelement.service.create.CreateRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementDto;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class RcdElementApi {
    private final RequestSpecification spec;

    public Response create(CreateRcdElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(RcdElementController.BASE_URL);
    }

    public Response update(String rcdElementId, UpdateRcdElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(RcdElementController.BASE_URL + "/{rcdElementId}", rcdElementId);
    }

    public Response deleteById(String rcdElementId) {
        return given(spec)
                .delete(RcdElementController.BASE_URL + "/{rcdElementId}", rcdElementId);
    }
}

