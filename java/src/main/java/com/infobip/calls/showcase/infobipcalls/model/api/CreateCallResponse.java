package com.infobip.calls.showcase.infobipcalls.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCallResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private State state;

    public enum State {
        CALLING, FAILED
    }
}
