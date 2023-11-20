# MYIO App

[//]: # (### Reference Documentation)

[//]: # (For further reference, please consider the following sections:)

[//]: # ()
[//]: # (* [Official Apache Maven documentation]&#40;https://maven.apache.org/guides/index.html&#41;)

[//]: # (* [Spring Boot Maven Plugin Reference Guide]&#40;https://docs.spring.io/spring-boot/docs/2.7.17/maven-plugin/reference/html/&#41;)

[//]: # (* [Create an OCI image]&#40;https://docs.spring.io/spring-boot/docs/2.7.17/maven-plugin/reference/html/#build-image&#41;)

[//]: # (* [Spring Web]&#40;https://docs.spring.io/spring-boot/docs/2.7.17/reference/htmlsingle/index.html#web&#41;)

[//]: # (* [Spring Boot DevTools]&#40;https://docs.spring.io/spring-boot/docs/2.7.17/reference/htmlsingle/index.html#using.devtools&#41;)

[//]: # ()
[//]: # (### Guides)

[//]: # (The following guides illustrate how to use some features concretely:)

[//]: # ()
[//]: # (* [Building a RESTful Web Service]&#40;https://spring.io/guides/gs/rest-service/&#41;)

[//]: # (* [Serving Web Content with Spring MVC]&#40;https://spring.io/guides/gs/serving-web-content/&#41;)

[//]: # (* [Building REST services with Spring]&#40;https://spring.io/guides/tutorials/rest/&#41;)

* ### What is MYIO(my income and outcome) ?
  myio application is an application used to track your income and outcome
* ### How to testing this backend application ?
  * creating environment variable in your favorit IDE such as intellij :
    ```HOST=yourlocalhost; DB_NAME=yourdbname; DB_PASSWORD=yourdbpassword; DB_USERNAME=yourusernamedb.``` 
    Open file ```application.properties ``` to see more.
  * create a database with the name you created in the env variable previously (this app using postgre)
  * Running entrypoint file ```MyincomeoutcomeApplication``` . This app will running in port 8081.
  * Open http://localhost:8081/swagger-ui/index.html to see these endpoints.
  * Please Register your account before testing this appilcation.
  * After register, login using your previously account, and copy the jwt token to the authorize menu.
  