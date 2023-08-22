package com.bricklink.api.rest.client;

import com.bricklink.api.rest.exception.BricklinkServerException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatCode;

@WireMockTest(httpPort = 8080)
class ErrorHandlingTest extends BricklistRestClientTest {
    @Test
    void invalidUri_returns() {
        stubFor(get(urlEqualTo("/orders/x"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getTestResponse("/__rest/bricklink/errors/invalid-endpoint-uri_200.json"))));
        assertThatCode(() -> {
            Object o = bricklinkRestClient.getOrder("x").getData();
        }).isInstanceOf(BricklinkServerException.class)
          .hasMessageContaining("INTERNAL_SERVER_ERROR")
          .hasMessageContaining("500");
    }
}
