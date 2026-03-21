package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElementController;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class OvercurrentProtectionElementApi {
    private final RequestSpecification spec;

    public Response create(CreateOvercurrentProtectionElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(OvercurrentProtectionElementController.BASE_URL);
    }

    public Response update(String overcurrentProtectionElementId, UpdateOvercurrentProtectionElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(OvercurrentProtectionElementController.BASE_URL + "/{overcurrentProtectionElementId}", overcurrentProtectionElementId);
    }

    public Response deleteById(String overcurrentProtectionElementId) {
        return given(spec)
                .delete(OvercurrentProtectionElementController.BASE_URL + "/{overcurrentProtectionElementId}", overcurrentProtectionElementId);
    }
}

