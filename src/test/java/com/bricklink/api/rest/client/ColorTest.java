package com.bricklink.api.rest.client;

import com.bricklink.api.rest.model.v1.Color;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
class ColorTest extends BricklistRestClientTest {
    @Test
    void colorById_returns200() {
        stubFor(get(urlEqualTo("/colors"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getTestResponse("/__rest/bricklink/color/colors/colors_200.json"))));
        List<Color> colors = bricklinkRestClient.getColors().getData();
        assertThat(colors.size()).isEqualTo(2);
    }

    @Test
    void getColor_withExistingColorId_returnsColor() {
        stubFor(get(urlMatching("/colors/[0-9]+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getTestResponse("/__rest/bricklink/color/colors/colors-by-id-01.json"))));
        Color color = bricklinkRestClient.getColor(1).getData();
        assertThat(color).isNotNull();
        assertThat(color.getColor_id()).isEqualTo(1);
        assertThat(color.getColor_name()).isEqualTo("Color 1");
    }
}

