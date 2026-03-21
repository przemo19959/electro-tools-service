package pl.dabrowski.electrotools.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElementController;
import pl.dabrowski.electrotools.elements.terminalelement.service.create.CreateTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementDto;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class TerminalElementApi {
    private final RequestSpecification spec;

    public Response create(CreateTerminalElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .post(TerminalElementController.BASE_URL);
    }

    public Response update(String terminalElementId, UpdateTerminalElementDto body) {
        return given(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .put(TerminalElementController.BASE_URL + "/{terminalElementId}", terminalElementId);
    }

    public Response deleteById(String terminalElementId) {
        return given(spec)
                .delete(TerminalElementController.BASE_URL + "/{terminalElementId}", terminalElementId);
    }
}

