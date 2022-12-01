package com.infobip.calls.showcase.webhook;

import com.infobip.calls.showcase.infobipcalls.CallsApiClient;
import com.infobip.calls.showcase.infobipcalls.model.SayRequest;
import com.infobip.calls.showcase.webhook.model.CallEstablishedEvent;
import com.infobip.calls.showcase.webhook.model.CallReceivedEvent;
import com.infobip.calls.showcase.webhook.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.infobip.calls.showcase.webhook.model.Call.Direction.INBOUND;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final CallsApiClient callsApiClient;

    @CrossOrigin
    @PostMapping("/call-received")
    public void handleCallReceivedEvent(@RequestBody CallReceivedEvent callReceivedEvent) {
        log.info("Received {}", callReceivedEvent);
        callsApiClient.answer(callReceivedEvent.getCallId());
    }

    @CrossOrigin
    @PostMapping("/event")
    public void handleEvent(@RequestBody Event event) {
        log.info("Received {}", event);

        if (!(event instanceof CallEstablishedEvent callEstablishedEvent)) {
            return;
        }

        if (callEstablishedEvent.getProperties().getCall().getDirection() == INBOUND) {
            callsApiClient.say(callEstablishedEvent.getCallId(), new SayRequest("Hello world", "en"));
        }
    }

}
