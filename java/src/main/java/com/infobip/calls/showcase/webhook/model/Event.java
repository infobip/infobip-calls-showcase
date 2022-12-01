package com.infobip.calls.showcase.webhook.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CallReceivedEvent.class, name = "CALL_RECEIVED"),
        @JsonSubTypes.Type(value = CallEstablishedEvent.class, name = "CALL_ESTABLISHED"),
        @JsonSubTypes.Type(value = CallFinishedEvent.class, name = "CALL_FINISHED"),
        @JsonSubTypes.Type(value = CallFailedEvent.class, name = "CALL_FAILED")
})
public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String callId;
    private Type type;

    public enum Type {
        CALL_RECEIVED, CALL_ESTABLISHED, CALL_FINISHED, CALL_FAILED
    }
}
