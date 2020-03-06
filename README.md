# IOUCorDapp
* #### A fork and updated version of [cordapp-template-kotlin](https://github.com/corda/cordapp-template-kotlin).  
* #### Code contents are from [bootcamp-cordapp](https://github.com/corda/bootcamp-cordapp) (A Java version), convert to Kotlin in this project.  
* #### Boot-camp tutorial video from YouTube [Corda Bootcamp](https://www.youtube.com/playlist?list=PLi1PppB3-YrWXZEtOnp0pyLnnP2zjJCZe)  
* #### Running the web server by following the [guideline](https://github.com/corda/samples/blob/release-V4/spring-webserver/README.md)  
* #### Valuable CorDapp [examples](https://github.com/corda/samples)
* #### Run procedures:
    1. `.\gradlew.bat deployNodes`
    2. `./build/nodes/runnodes`
    3. `.\gradlew.bat clients:runPartyAServer` (For spring boot)
* #### Run the spring boot server
    * Command: `.\gradlew.bat clients:runPartyAServer`  
    * Access: `http://localhost:10054/tempEndpoint?name=hahaha`
* #### Clean deployed nodes: 
    * `.\gradlew.bat clean deployNodes`  

