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

const AUTH = 'App ' + conf('INFOBIP_API_KEY');
const ARGS = require('args-parser')(process.argv);

app.post('/call-received', async (req, res) => {
    console.log('Received event: %s', JSON.stringify(req.body));
    console.log('New call from %s', req.body.properties.call.from);
    let callId = req.body.callId;
    try {
        await answer(callId);
        res.sendStatus(200)
    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

app.post('/event', async (req, res) => {
    console.log('Received event: %s', JSON.stringify(req.body));
    let type = req.body.type;

    if (type === 'CALL_ESTABLISHED' && req.body.properties.call.direction === 'INBOUND') {
        let callId = req.body.callId;
        try {
            console.log('Saying text...');
            await say(callId, "Hello world, this is test!", "en");
            res.sendStatus(200)
        } catch (err) {
            console.error(err);
            res.sendStatus(500);
        }
    } else if (type === 'SAY_FINISHED') {
        let callId = req.body.callId;
        try {
            console.log('Hanging up...');
            await hangupCall(callId);
            res.sendStatus(200)
        } catch (err) {
            console.error(err);
            res.sendStatus(500);
        }
    } else if (type === 'CALL_FINISHED') {
        console.log('Call finished with error code %s', req.body.properties.callLog.errorCode.name);
        res.sendStatus(200);

        const conferenceIds = req.body.properties.callLog.conferenceIds;
        if (conferenceIds && conferenceIds[0]) {
            const conferenceId = conferenceIds[0];
            await hangupConference(conferenceId);
        }

        server.close();
    } else if (type === 'CALL_FAILED') {
        console.error('Call failed with error code %s', req.body.properties.callLog.errorCode.name);
        res.sendStatus(200);

        const conferenceIds = req.body.properties.callLog.conferenceIds;
        if (conferenceIds && conferenceIds[0]) {
            const conferenceId = conferenceIds[0];
            await hangupConference(conferenceId);
        }

        server.close();
    } else {
        res.sendStatus(200);
    }
});

const server = app.listen(conf('HTTP_PORT'), () => {
    console.log('Started InfobipCallsShowcase at http://%s:%s', 'localhost', conf('HTTP_PORT'));

    if (ARGS['scenario'] === 'inbound') {
        console.log('Waiting for inbound call...')
        return;
    }

    const firstPhoneNumber = ARGS['scenario.first-number'];
    const secondPhoneNumber = ARGS['scenario.second-number'];

    console.log('Starting outbound scenario with numbers %s and %s...', firstPhoneNumber, secondPhoneNumber);

    Promise.all([
        callPhone(secondPhoneNumber, firstPhoneNumber),
        callPhone(firstPhoneNumber, secondPhoneNumber)
    ]).then(callResponses => {
        const firstCallResponse = callResponses[0];
        console.log('Response calling %s: %s', firstPhoneNumber, JSON.stringify(firstCallResponse));

        const secondCallResponse = callResponses[1];
        console.log('Response calling %s: %s', secondPhoneNumber, JSON.stringify(secondCallResponse));

        console.log('Connecting calls %s and %s...', firstCallResponse.id, secondCallResponse.id);
        connect([firstCallResponse.id, secondCallResponse.id]).then(connectResponse => {
            console.log('Response connecting %s and %s: %s', firstCallResponse.id, secondCallResponse.id, JSON.stringify(connectResponse));
            setTimeout(() => {
                console.log('Hanging up...');
                hangupConference(connectResponse.conferenceId);
            }, 30000);
        });
    });

});

async function callPhone(callerId, phoneNumber) {
    console.log('Calling %s...', phoneNumber);
    let body = JSON.stringify({
        applicationId: conf('INFOBIP_APP_ID'),
        from: callerId,
        endpoint: {
            type: "PHONE",
            phoneNumber: phoneNumber
        }
    });
    try {
        const createCallResponse = await https.post(conf('INFOBIP_API_HOST'), "/calls/1/calls", body, AUTH);
        let response = JSON.parse(createCallResponse);
        return {
            id: response.id,
            state: response.state
        };
    } catch (err) {
        console.error(err);
    }
}

async function connect(callIds) {
    let body = JSON.stringify({
        callIds: callIds
    });
    try {
        const connectCallsResponse = await https.post(conf('INFOBIP_API_HOST'), "/calls/1/connect", body, AUTH);
        let response = JSON.parse(connectCallsResponse);
        return {
            conferenceId: response.id
        };
    } catch (err) {
        console.error(err);
    }
}

async function answer(callId) {
    const answerResponse = await https.post(conf('INFOBIP_API_HOST'), "/calls/1/calls/" + callId + "/answer", "{}", AUTH);
    return JSON.parse(answerResponse);
}

async function say(callId, text, language) {
    let body = JSON.stringify({
        text: text,
        language: language
    });
    const sayResponse = await https.post(conf('INFOBIP_API_HOST'), "/calls/1/calls/" + callId + "/say", body, AUTH);
    return JSON.parse(sayResponse);
}

async function hangupCall(callId) {
    await https.post(conf('INFOBIP_API_HOST'), "/calls/1/calls/" + callId + "/hangup", "{}", AUTH)
}

async function hangupConference(conferenceId) {
    await https.post(conf('INFOBIP_API_HOST'), "/calls/1/conferences/" + conferenceId + "/hangup", "{}", AUTH)
}

function conf(key) {
    return config[key] || process.env[key];
}