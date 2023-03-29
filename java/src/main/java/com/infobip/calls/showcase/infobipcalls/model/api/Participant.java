package com.infobip.calls.showcase.infobipcalls.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participant implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String callId;
    private Endpoint endpoint;
    private State state;
    private String joinTime;

    public enum State {
        JOINING, JOINED
    }
}
