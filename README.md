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

In order to test this scenario, you need public webhook URLs for `CALL_RECEIVED` and other Calls events. Follow these steps to try it out:

#### Step 1
Run application with `--scenario=inbound` mode.

#### Step 2
Expose your application with its webhooks to public internet. Easiest way to do so is via [ngrok](https://ngrok.com/). After installing it, just run:
```shell
ngrok http 8080
```
in your terminal, and you will get nice public URL.

#### Step 3
Create a Calls Configuration and a matching event subscription for this showcase:

1. Create a [Calls Configuration](https://www.infobip.com/docs/voice-and-video/calls) and choose a `callsConfigurationId`.
2. Create a `VOICE_VIDEO` subscription with the same `callsConfigurationId` in the subscription criteria.
3. Configure your webhook URLs to point to:
   - `https://${your-ngrok-id}.ngrok.io/call-received`
   - `https://${your-ngrok-id}.ngrok.io/event`
4. Subscribe at least to:
   - `CALL_RECEIVED`
   - `CALL_ESTABLISHED`
   - `SAY_FINISHED`
   - `CALL_FINISHED`
   - `CALL_FAILED`

You can create both the Calls Configuration and the subscription via the Infobip web interface or API. The current Calls setup flow is documented in:

- [Calls overview and setup](https://www.infobip.com/docs/voice-and-video/calls)
- [Create and manage subscriptions](https://www.infobip.com/docs/cpaas-x/subscriptions-management/create-manage-subscriptions)

#### Step 4
Purchase your own DID number and configure its voice action to **Forward to Subscription** using the same `callsConfigurationId` from Step 3.  
You can set up your own number via our [public endpoint](https://www.infobip.com/docs/api/platform/numbers/my-numbers/number-management/create-voice-setup-on-number) or in the Infobip web interface.

In application logs you can find new entry when new call is received. There you can find `callId` of your inbound call.  
Implemented behaviour includes answering inbound call and saying `Hello world, this is test!` in English language.
