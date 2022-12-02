const express = require('express');
const config = require('./config.json');
const https = require('./lib/https')();
const app = express();
app.use(express.json());

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
const AUTH = 'App ' + config.INFOBIP_API_KEY;

app.post('/call', async (req, res) => {
    let callerId = req.body.callerId;
    let phoneNumber = req.body.phoneNumber;
    let body = JSON.stringify({
        applicationId: config.INFOBIP_APP_ID,
        from: callerId,
        endpoint: {
            type: "PHONE",
            phoneNumber: phoneNumber
        }
    });
    try {
        const createCallResponse = await https.post(config.INFOBIP_API_HOST, "/calls/1/calls", body, AUTH);
        console.log('Received response: ' + createCallResponse);
        let response = JSON.parse(createCallResponse);
        res.json({
            id: response.id,
            state: response.state
        });
    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

app.post('/connect', async (req, res) => {
    let callIds = req.body.callIds;
    let body = JSON.stringify({
        callIds: callIds
    });
    try {
        const connectCallsResponse = await https.post(config.INFOBIP_API_HOST, "/calls/1/connect", body, AUTH);
        console.log('Received response: ' + connectCallsResponse);
        let response = JSON.parse(connectCallsResponse);
        res.json({
            conferenceId: response.id
        });
    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

app.post('/call-received', async (req, res) => {
    console.log("Received event: " + JSON.stringify(req.body));
    let callId = req.body.callId;
    try {
        const answerResponse = await https.post(config.INFOBIP_API_HOST, "/calls/1/calls/" + callId + "/answer", "{}", AUTH);
        console.log('Received response: ' + answerResponse);
        res.sendStatus(200)
    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

app.post('/event', async (req, res) => {
    console.log("Received event: " + JSON.stringify(req.body));
    let type = req.body.type;
    if (type !== 'CALL_ESTABLISHED') {
        res.sendStatus(200);
        return;
    }

    let callId = req.body.callId;
    let body = JSON.stringify({
        text: "Hello world",
        language: "en"
    });
    try {
        const sayResponse = await https.post(config.INFOBIP_API_HOST, "/calls/1/calls/" + callId + "/say", body, AUTH);
        console.log('Received response: ' + sayResponse);
        res.sendStatus(200)
    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

app.listen(config.HTTP_PORT, () => console.log('InfobipCallsShowcase started at: http://%s:%s', 'localhost', config.HTTP_PORT));