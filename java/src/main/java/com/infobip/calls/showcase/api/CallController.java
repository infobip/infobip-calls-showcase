package com.infobip.calls.showcase.api;

import com.infobip.calls.showcase.api.model.CallRequest;
import com.infobip.calls.showcase.api.model.CallResponse;
import com.infobip.calls.showcase.api.model.ConnectRequest;
import com.infobip.calls.showcase.api.model.ConnectResponse;
import com.infobip.calls.showcase.infobipcalls.CallsApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CallController {

    private final CallsApiClient callsApiClient;

    @CrossOrigin
    @PostMapping("/call")
    public CallResponse generate(@RequestBody CallRequest callRequest) {
        log.info("Received {}", callRequest);
        return callsApiClient.callPhone(callRequest);
    }

    @CrossOrigin
    @PostMapping("/connect")
    public ConnectResponse generate(@RequestBody ConnectRequest connectRequest) {
        log.info("Received {}", connectRequest);
        return callsApiClient.connect(connectRequest);
    }
}
