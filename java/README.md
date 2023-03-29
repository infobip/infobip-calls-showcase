# Running

#### Prerequisites
* Installed [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
* Installed JDK 17 or newer. Check version with `java -version`.
* Installed [maven](https://maven.apache.org/install.html).

#### Step 1
Clone this application from [github repository](https://github.com/infobip/infobip-calls-showcase):
```shell
git clone git@github.com:infobip/infobip-calls-showcase.git
```
#### Step 2
Edit [`application.yml`](./src/main/resources/application.yml) file, set your own values for `infobip.api-key` and `infobip.application-id` keys.  
You can also leave file untouched, and set `INFOBIP_API_KEY` and `INFOBIP_APP_ID` environment variables prior to starting application in step 4.

#### Step 3
Compile and build application using this command:
```shell
mvn clean package
```
Now you will have executable jar `infobip-calls-showcase.jar` in your `java/target` directory.

#### Step 4
Start application with chosen mode. You need to pass `--scenario` parameter with value `inbound` or `outbound`.  
If you chose `outbound`, you need to pass additional `--scenario.first-number` and `--scenario.second-number` parameters.  
Example:
```shell
java -jar target/infobip-calls-showcase.jar --scenario=outbound --scenario.first-number=123 --scenario.second-number=456
```
This will run outbound scenario described [here](../README.md).

In application logs you can follow scenario progress.