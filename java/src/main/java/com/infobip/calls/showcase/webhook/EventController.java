package com.infobip.calls.showcase.webhook;

import com.infobip.calls.showcase.infobipcalls.CallsApiClient;
import com.infobip.calls.showcase.infobipcalls.model.api.SayRequest;
import com.infobip.calls.showcase.webhook.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.infobip.calls.showcase.webhook.model.Call.Direction.INBOUND;
import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final CallsApiClient callsApiClient;

    @CrossOrigin
    @PostMapping("/call-received")
    public void handleCallReceived(@RequestBody CallReceivedEvent event) {
        log.info("Received {}", event);
        handleCallReceivedEvent(event);
    }

    @CrossOrigin
    @PostMapping("/event")
    public void handle(@RequestBody Event event) {
        log.info("Received {}", event);

        if (event instanceof CallEstablishedEvent callEstablishedEvent) {
            handleCallEstablishedEvent(callEstablishedEvent);
        }

        if (event instanceof SayFinishedEvent sayFinishedEvent) {
            handleSayFinishedEvent(sayFinishedEvent);
        }

        if (event instanceof CallFinishedEvent callFinishedEvent) {
            handleCallFinishedEvent(callFinishedEvent);
        }

        if (event instanceof CallFailedEvent callFailedEvent) {
            handleCallFailedEvent(callFailedEvent);
        }
    }

    private void handleCallReceivedEvent(CallReceivedEvent callReceivedEvent) {
        log.info("New call from {}", callReceivedEvent.getProperties().getCall().getFrom());
        callsApiClient.answer(callReceivedEvent.getCallId());
    }

    private void handleCallEstablishedEvent(CallEstablishedEvent callEstablishedEvent) {
        if (callEstablishedEvent.getProperties().getCall().getDirection() == INBOUND) {
            log.info("Saying text...");
            callsApiClient.say(callEstablishedEvent.getCallId(), new SayRequest("Hello world, this is test!", "en"));
        }
    }

    private void handleSayFinishedEvent(SayFinishedEvent sayFinishedEvent) {
        log.info("Hanging up...");
        callsApiClient.hangupCall(sayFinishedEvent.getCallId());
    }

    private void handleCallFinishedEvent(CallFinishedEvent callFinishedEvent) {
        log.info("Call finished with error code {}", callFinishedEvent.getProperties().getCallLog().getErrorCode().getName());

        var conferenceIds = callFinishedEvent.getProperties().getCallLog().getConferenceIds();
        if (nonNull(conferenceIds) && conferenceIds.size() == 1) {
            var conferenceId = conferenceIds.get(0);
            callsApiClient.hangupConference(conferenceId);
        }
        
        System.exit(0);
    }

    private void handleCallFailedEvent(CallFailedEvent callFailedEvent) {
        log.error("Call failed with error code {}", callFailedEvent.getProperties().getCallLog().getErrorCode().getName());

        var conferenceIds = callFailedEvent.getProperties().getCallLog().getConferenceIds();
        if (nonNull(conferenceIds) && conferenceIds.size() == 1) {
            var conferenceId = conferenceIds.get(0);
            callsApiClient.hangupConference(conferenceId);
        }

        System.exit(1);
    }

}
