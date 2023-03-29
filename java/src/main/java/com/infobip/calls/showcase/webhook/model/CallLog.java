package com.infobip.calls.showcase.webhook.model;

import com.infobip.calls.showcase.infobipcalls.model.api.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String callId;
    private Instant startTime;
    private Instant answerTime;
    private Instant endTime;
    private Endpoint endpoint;
    private String from;
    private String to;
    private Direction direction;
    private ErrorCode errorCode;
    private int duration;
    private List<String> conferenceIds;

    public enum Direction {
        INBOUND, OUTBOUND
    }
}
