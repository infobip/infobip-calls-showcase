package com.infobip.calls.showcase.infobipcalls.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectCallsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private List<Participant> participants;

}
