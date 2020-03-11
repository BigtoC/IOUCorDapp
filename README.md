# IOUCorDapp

* #### A fork and updated version of [cordapp-template-kotlin](https://github.com/corda/cordapp-template-kotlin).  
* #### Code contents are from [bootcamp-cordapp](https://github.com/corda/bootcamp-cordapp) (A Java version), convert to Kotlin in this project.  
* #### Boot-camp tutorial video from YouTube [Corda Bootcamp](https://www.youtube.com/playlist?list=PLi1PppB3-YrWXZEtOnp0pyLnnP2zjJCZe)  
* #### Running the web server by following the [guideline](https://github.com/corda/samples/blob/release-V4/spring-webserver/README.md)  
* #### Valuable CorDapp [examples](https://github.com/corda/samples)
* #### Run procedures:
    1. Deploy nodes: 
       * Win - `.\gradlew.bat deployNodes` 
       * Linux - `./gradlew deployNodes`  
    2. Run nodes: 
       * Win & Linux : `./build/nodes/runnodes`
    3. Run a server: 
       * Win: `.\gradlew.bat clients:runPartyAServer`
       * Linux: `./gradlew clients:runPartyAServer`  
    4. Run the spring boot server
       1. Command: 
          * Win: `.\gradlew.bat clients:runPartyAServer`  
          * Linux: `./gradlew clients:runPartyAServer`
       2. Access via browser: `http://localhost:10054/tempEndpoint?name=hahaha`
* #### Clean deployed nodes: 
    * Win: `.\gradlew.bat clean deployNodes`  
    * Linux: `./gradlew clean deployNodes`  

