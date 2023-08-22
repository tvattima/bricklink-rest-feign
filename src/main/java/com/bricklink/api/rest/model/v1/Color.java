package com.bricklink.api.rest.model.v1;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Color {
    private Integer color_id;
    private String color_name;
    private String color_code;
    private String color_type;
}
