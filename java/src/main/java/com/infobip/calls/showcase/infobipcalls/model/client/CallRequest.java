package com.infobip.calls.showcase.infobipcalls.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String phoneNumber;
    private String callerId;

}
