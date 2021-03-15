FROM openjdk:11.0.10-slim
VOLUME /tmp
COPY ./build/libs/portfolio-server-1.0.0.jar app.jar
SHELL ["pwd"]
ENTRYPOINT ["java","-jar","/app.jar"]