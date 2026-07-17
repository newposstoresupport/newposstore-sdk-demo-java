# Apply for a newposstore account #
Use your email to register an account on the NEWSTORE developer platform and apply for enterprise developer certification. The website of the NEWSTORE platform is：[https://newposstore.com/account/login?redirect=/account](https://newposstore.com/account/login?redirect=/account "NEWPOSSTORE")
# Create capability application #
The operation steps for creating an ability application can be referred to in the current directory files "createAbilityApp.wmv".
<video src="./files/createAbilityApp.wmv" controls width="600">
  Your browser does not support video playback. Please switch to another browser.Your browser does not support video playback. Please switch to another browser and try again.
</video>

# Configure relevant parameters #
## config appKey infomation ##
The parameters such as AppKey obtained through creating the capability application need to be configured in the  file of the app module of the current project. Of course, this is a demo project. The specific configuration location should be according to the requirements of the third-party APP project.

After you complete the creation of the capability application on the NewStore platform, by clicking the "Copy" button as shown in the figure below, you will obtain the corresponding APPKey information.

![](./files/appkeyinfo4.png)

```javascript
{"appId":"5d3a7c9cf5ef0ae9984a46a298f137ec","appKey":"9231bdfa47f2e1a8aeadad51528201b6","appSecret":"9c4b04cbe7b9ddd99052c6d470db98d7"}
```

In the local.properties configuration file of the project, configure the corresponding appkey information as shown in the following figure.

![](./files/appkeyinfo.png)

![](./files/appkeyinfo2.png)

![](./files/appkeyinfo3.png)



Following the above steps, the corresponding APPKEY information has been successfully configured in the DEMO project, and you can now proceed with the corresponding tests.

## modify appliciationId ##
Modify the applicationId in the project's build.gradle file to the package name configured when creating the capability application.



# Do Test #
Currently, the APP testing relies on the NEWPOS intelligent terminal, and the terminal needs to install the NEWPOSSTORE app. For the intelligent terminal equipment and related installation package APP, you can contact newposstore@newpostech.com for acquisition.

According to the above process, the ApplicatinoId you have applied for is the applicationId corresponding to the channel package configuration in the build.grale script. This value must be consistent with the package name filled in when creating the capability application.

# Notes for Attention #
- The signature fingerprint information filled in when creating the capability application is the SHA256 fingerprint information of the signature certificate, represented as a 16-digit hexadecimal string, with colons separating the digits.
- The signature information during the program's operation needs to be consistent with that when creating the capability application. Therefore, you need to replace the signature certificate information in DEMO with your own.