package com.infobip.calls.showcase.infobipcalls.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SayRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String text;
    private String language;

}
