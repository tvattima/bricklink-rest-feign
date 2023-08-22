package com.bricklink.api.rest.service;

import com.bricklink.api.rest.client.BricklinkRestClient;
import com.bricklink.api.rest.model.v1.BricklinkMeta;
import com.bricklink.api.rest.model.v1.BricklinkResource;
import com.bricklink.api.rest.model.v1.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ColorServiceTest {
    @Test
    void colorById() {
        BricklinkRestClient bricklinkRestClient = mock(BricklinkRestClient.class);
        setupColorsList(bricklinkRestClient);
        BricklinkResource<Color> color = getResource(200, color(1, "1", "White"));
        doReturn(color).when(bricklinkRestClient).getColor(1);
        ColorService colorService = new ColorService(bricklinkRestClient);
        Color c = colorService.getColorById(1);
        Color c1 = colorService.getColorByName("White");
        assertThat(c).isSameAs(c1);

        Color c2 = colorService.getColorByName("Reddish-Brown");
        assertThat(c2.getColor_name()).isEqualTo("Reddish-Brown");
        Color c3 = colorService.getColorByName("Reddish Brown");
        assertThat(c2).isSameAs(c3);
    }

    private Color color(final Integer colorId, final String colorCode, final String colorName) {
        Color color = new Color();
        color.setColor_id(colorId);
        color.setColor_code(colorCode);
        color.setColor_name(colorName);
        return color;
    }

    private <T> BricklinkResource<T> getResource(final int code, T t) {
        BricklinkResource<T> resource = new BricklinkResource<>();
        BricklinkMeta meta = new BricklinkMeta();
        meta.setCode(code);
        resource.setMeta(meta);
        resource.setData(t);
        return resource;
    }

    private void setupColorsList(final BricklinkRestClient bricklinkRestClient) {
        List<Color> colors = new ArrayList<>();
        colors.add(color(1, "1", "White"));
        colors.add(color(2, "2", "Tan"));
        colors.add(color(3, "3", "Yellow"));
        colors.add(color(4, "4", "Orange"));
        colors.add(color(5, "5", "Red"));
        colors.add(color(6, "6", "Reddish-Brown"));
        doReturn(getResource(200, colors)).when(bricklinkRestClient).getColors();
    }
}