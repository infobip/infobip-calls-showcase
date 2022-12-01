# Infobip Calls Showcase

This service is used to showcase features of Infobip Calls API.  
It enables you to start outbound call towards some phone number, and to handle incoming call from some phone number.  

### Starting outbound call

You can start new outbound call towards some phone number by invoking this API exposed by this service:

```http request
POST /call
Content-Type: application/json

{
  "phoneNumber": "${phone-number}",
  "callerId": "${caller-id}"
}
```

You can see that you can specify optional `callerId` request parameter. We will use it to display it as Caller ID when delivering call to phone.

In response, you will receive `callId` of created outbound call.

You can create as many outbound calls as you want.

### Connecting calls

You can connect multiple calls in single conference via HTTP API method that this service exposes:

```http request
POST /connect
Content-Type: application/json

{
  "callIds": [
    "${callId-1}",
    "${callId-2},
    ...
    "${callId-n}"
  ]
}
```

### Receiveing inbound call

You can call number `38761456296` that we preconfigured for you.  
In service logs you can find new entry when new call is received. There you can find `callId` of your inbound call.  
Implemented behaviour includes answering inbound call and saying `Hello world` in English language.

In order to actually try this, you need to expose Webhook endpoints to public internet and set corresponding
URLs in [application configuration](https://www.infobip.com/docs/api/channels/voice/calls/calls-applications/get-calls-application).

Easiest way to do so is via [ngrok](https://ngrok.com/). After installing it, just run:
```shell
ngrok http 8080
```
in your terminal and you will get nice public URL.  

Now it's time to edit your application via [our API method](https://www.infobip.com/docs/api/channels/voice/calls/calls-applications/update-calls-application):  
_(Don't forget to replace `${your-ngrok-id}` with your unique ID obtained via `ngrok http 8080`)_
```http request
PUT /calls/1/applications/abc-def-ghi
Content-Type: application/json

{
  "name": "Infobip Calls Showcase",
  "subscribedEvents": [
    "CALL_RECEIVED", "CALL_ESTABLISHED", "CALL_FINISHED", "CALL_FAILED"
  ],
  "webhook": {
    "receiveUrl": "https://${your-ngrok-id}.ngrok.io/call-received",
    "eventUrl": "https://${your-ngrok-id}.ngrok.io/event"
  }
}
```