package com.infobip.calls.showcase.infobipcalls.model.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static com.infobip.calls.showcase.infobipcalls.model.api.Endpoint.Type.PHONE;

@Data
@NoArgsConstructor
public class Endpoint implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Type type = PHONE;
    private String phoneNumber;

    public Endpoint(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public enum Type {
        PHONE
    }
}
