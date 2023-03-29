package com.infobip.calls.showcase.webhook.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallFinishedProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private CallLog callLog;

}
