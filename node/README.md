# Running

#### Prerequisites
* Installed [Node.js](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).
* Installed [npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).

#### Step 1
Clone this application from [github repository](https://github.com/infobip/infobip-calls-showcase):
```shell
git clone git@github.com:infobip/infobip-calls-showcase.git
```

#### Step 2
Install application:
```shell
npm install
```

#### Step 3
Edit [`config.json`](./config.json) file, set your own values for `INFOBIP_API_KEY` and `INFOBIP_APP_ID` keys.  
You can also leave file untouched, and set `INFOBIP_API_KEY` and `INFOBIP_APP_ID` environment variables prior to starting application in step 4.

#### Step 4
Start application with chosen mode. You need to pass `--scenario` parameter with value `inbound` or `outbound`.  
If you chose `outbound`, you need to pass additional `--scenario.first-number` and `--scenario.second-number` parameters.  

You can start it via `npm`'s `start` script via this command:
```shell
npm start -- --scenario=outbound --scenario.first-number=123 --scenario.second-number=456
```
Or directly via `node`:
```shell
node app.js --scenario=outbound --scenario.first-number=123 --scenario.second-number=456
```

This will run outbound scenario described [here](../README.md).

In application logs you can follow scenario progress.