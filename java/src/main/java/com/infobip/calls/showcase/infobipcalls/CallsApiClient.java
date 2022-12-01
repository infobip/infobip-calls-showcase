package com.infobip.calls.showcase.infobipcalls;

import com.infobip.calls.showcase.api.model.CallRequest;
import com.infobip.calls.showcase.api.model.CallResponse;
import com.infobip.calls.showcase.api.model.ConnectRequest;
import com.infobip.calls.showcase.api.model.ConnectResponse;
import com.infobip.calls.showcase.infobipcalls.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static com.infobip.calls.showcase.infobipcalls.model.ActionStatus.FAILED;

@Service
public class CallsApiClient {

    private final String baseUrl;
    private final String applicationId;
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders httpHeaders;

    public CallsApiClient(@Value("https://${infobip.api-host}") String infobipBaseUrl,
                          @Value("${infobip.api-key}") String infobipApiKey,
                          @Value("${infobip.application-id}") String infobipApplicationId) {
        this.baseUrl = infobipBaseUrl;
        this.applicationId = infobipApplicationId;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set(HttpHeaders.AUTHORIZATION, "App " + infobipApiKey);
    }

    public CallResponse callPhone(CallRequest callRequest) {
        var createCallRequest = new CreateCallRequest(applicationId, new Endpoint(callRequest.getPhoneNumber()), callRequest.getCallerId());
        var httpRequest = new HttpEntity<>(createCallRequest, httpHeaders);
        var httpResponse = restTemplate.postForEntity("%s/calls/1/calls".formatted(baseUrl), httpRequest, CreateCallResponse.class);
        return Optional.ofNullable(httpResponse.getBody()).map(createCallResponse -> new CallResponse(createCallResponse.getId())).orElse(new CallResponse("n/a"));
    }

    public ConnectResponse connect(ConnectRequest connectRequest) {
        var connectCallsRequest = new ConnectCallsRequest(connectRequest.getCallIds());
        var httpRequest = new HttpEntity<>(connectCallsRequest, httpHeaders);
        var httpResponse = restTemplate.postForEntity("%s/calls/1/connect".formatted(baseUrl), httpRequest, ConnectCallsResponse.class);
        return Optional.ofNullable(httpResponse.getBody()).map(connectCallsResponse -> new ConnectResponse(connectCallsResponse.getId())).orElse(new ConnectResponse("n/a"));
    }

    public void answer(String callId) {
        var httpRequest = new HttpEntity<>(Map.of(), httpHeaders);
        restTemplate.postForEntity("%s/calls/1/calls/%s/answer".formatted(baseUrl, callId), httpRequest, Void.class);
    }

    public SayResponse say(String callId, SayRequest sayRequest) {
        var httpRequest = new HttpEntity<>(sayRequest, httpHeaders);
        var httpResponse = restTemplate.postForEntity("%s/calls/1/calls/%s/say".formatted(baseUrl, callId), httpRequest, SayResponse.class);
        return Optional.ofNullable(httpResponse.getBody()).orElse(new SayResponse(FAILED));
    }
}
