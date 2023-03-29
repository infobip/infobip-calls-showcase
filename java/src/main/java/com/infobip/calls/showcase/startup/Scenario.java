package com.infobip.calls.showcase.startup;

import com.infobip.calls.showcase.InfobipCallsShowcase;
import com.infobip.calls.showcase.infobipcalls.model.client.CallRequest;
import com.infobip.calls.showcase.infobipcalls.model.client.ConnectRequest;
import com.infobip.calls.showcase.infobipcalls.CallsApiClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scenario implements ApplicationRunner {

    private final CallsApiClient callsApiClient;

    @Override
    public void run(ApplicationArguments args) {
        var scenario = getSingleArg(args, "scenario");
        if ("inbound".equals(scenario)) {
            log.info("Waiting for inbound call...");
            return;
        }

        var firstPhoneNumber = getSingleArg(args, "scenario.first-number");
        var secondPhoneNumber = getSingleArg(args, "scenario.second-number");
        log.info("Starting outbound scenario with numbers {} and {}...", firstPhoneNumber, secondPhoneNumber);

        log.info("Calling {}...", firstPhoneNumber);
        var firstCallRequest = new CallRequest(secondPhoneNumber, firstPhoneNumber);
        var firstCallResponse = callsApiClient.callPhone(firstCallRequest);
        log.info("Response calling {}: {}", firstPhoneNumber, firstCallResponse);

        log.info("Calling {}...", secondPhoneNumber);
        var secondCallRequest = new CallRequest(firstPhoneNumber, secondPhoneNumber);
        var secondCallResponse = callsApiClient.callPhone(secondCallRequest);
        log.info("Response calling {}: {}", secondPhoneNumber, secondCallResponse);

        log.info("Connecting calls {} and {}...", firstCallResponse.getCallId(), secondCallResponse.getCallId());
        var connectRequest = new ConnectRequest(List.of(firstCallResponse.getCallId(), secondCallResponse.getCallId()));
        var connectResponse = callsApiClient.connect(connectRequest);
        log.info("Response connecting {} and {}: {}", firstCallResponse.getCallId(), secondCallResponse.getCallId(), connectResponse);

        sleep(30);

        log.info("Hanging up...");
        callsApiClient.hangupConference(connectResponse.getConferenceId());

        sleep(2);

        System.exit(0);
    }

    @SneakyThrows(InterruptedException.class)
    private void sleep(int nSeconds) {
        Thread.sleep(SECONDS.toMillis(nSeconds));
    }

    private String getSingleArg(ApplicationArguments args, String key) {
        var values = args.getOptionValues(key);
        if (isNull(values) || values.size() != 1) {
            log.error("{} not defined correctly!", key);
            System.exit(1);
        }
        return values.get(0);
    }
}
