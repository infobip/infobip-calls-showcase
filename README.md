# Infobip Calls Showcase

This application is used to showcase features of Infobip Calls API.  
It operates in one of two modes, outbound or inbound.  
In outbound mode, you provide two phone numbers on startup, and it will call those phones and connect them into conference.  
In inbound mode, it will wait until you make an inbound call, play some text and then hang up call.

### Outbound mode

You choose this mode by providing `--scenario=outbound` param.  
By providing `--scenario.first-number` and `--scenario.second-number` params, you choose which phones to call.  
You will be able to track scenario progress in console logs.

### Inbound mode
You choose this mode by providing `--scenario=inbound` param.  

In order to test this scenario, you need [public webhook](https://www.infobip.com/docs/api/channels/voice/calls/calls-applications/receive-calls) to handle `CALL_RECEIVED` event. Follow these steps to try it out:

#### Step 1
Run application with `--scenario=inbound` mode.

#### Step 2
Expose your application with its webhooks to public internet. Easiest way to do so is via [ngrok](https://ngrok.com/). After installing it, just run:
```shell
ngrok http 8080
```
in your terminal, and you will get nice public URL.

#### Step 3
Create Calls Application (or [edit](https://www.infobip.com/docs/api/channels/voice/calls/calls-applications/update-calls-application) one you previously created) with new URL obtained via ngrok.  
_(Don't forget to replace `${your-ngrok-id}` with your unique ID obtained via `ngrok http 8080`, `${your-application-id}` and `${your-api-key}`)_
```shell
curl -X PUT https://api.infobip.com/calls/1/applications/${your-application-id} \
  -H 'Authorization: App ${your-api-key}' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Infobip Calls Showcase",
    "subscribedEvents": [
      "CALL_RECEIVED", "CALL_ESTABLISHED", "SAY_FINISHED", "CALL_FINISHED", "CALL_FAILED"
    ],
    "webhook": {
      "receiveUrl": "https://${your-ngrok-id}.ngrok.io/call-received",
      "eventUrl": "https://${your-ngrok-id}.ngrok.io/event"
    }
  }'
```
#### Step 4
Purchase your own DID number and set it up with `applicationId` from Step 3.  
You can set up your own number via our [public endpoint](https://www.infobip.com/docs/api/platform/numbers/my-numbers/number-management/create-voice-setup-on-number).

In application logs you can find new entry when new call is received. There you can find `callId` of your inbound call.  
Implemented behaviour includes answering inbound call and saying `Hello world, this is test!` in English language.