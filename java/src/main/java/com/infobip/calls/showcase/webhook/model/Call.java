package com.infobip.calls.showcase.webhook.model;

import com.infobip.calls.showcase.infobipcalls.model.api.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Call implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private Endpoint endpoint;
    private String from;
    private String to;
    private Direction direction;

    public enum Direction {
        INBOUND, OUTBOUND
    }
}
