package pl.dabrowski.electrotools;

import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.dabrowski.electrotools.api.*;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    protected RequestSpecification givenRequest() {
        return given()
                .baseUri("http://localhost:" + port);
    }

    protected ProjectApi projectApi() {
        return new ProjectApi(givenRequest());
    }

    protected BasicElementApi basicElementApi() {
        return new BasicElementApi(givenRequest());
    }

    protected LoadElementApi loadElementApi() {
        return new LoadElementApi(givenRequest());
    }

    protected OvercurrentProtectionElementApi overcurrentProtectionElementApi() {
        return new OvercurrentProtectionElementApi(givenRequest());
    }

    protected RcdElementApi rcdElementApi() {
        return new RcdElementApi(givenRequest());
    }

    protected TerminalElementApi terminalElementApi() {
        return new TerminalElementApi(givenRequest());
    }
}