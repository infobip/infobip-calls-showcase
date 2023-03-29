package com.infobip.calls.showcase.webhook.model;

import lombok.*;

import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SayFinishedEvent extends Event {

    @Serial
    private static final long serialVersionUID = 1L;

    private SayFinishedProperties properties;

}
