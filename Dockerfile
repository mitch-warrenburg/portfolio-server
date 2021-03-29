FROM adoptopenjdk:11-jdk-openj9 as build

WORKDIR /workspace/app

COPY gradlew build.gradle ./
COPY src src/
COPY gradle gradle/

RUN ./gradlew clean build --max-workers 20 --parallel --console rich --info -x test --stacktrace
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM adoptopenjdk:11-jdk-openj9

ARG EMAIL_API_KEY
ARG EMAIL_SENDER_NAME
ARG EMAIL_SENDER_ADDRESS
ARG EMAIL_RECIPIENT_NAME
ARG EMAIL_RECIPIENT_ADDRESS

ENV EMAIL_API_KEY=${EMAIL_API_KEY} \
    EMAIL_SENDER_NAME=${EMAIL_SENDER_NAME} \
    EMAIL_SENDER_ADDRESS=${EMAIL_SENDER_ADDRESS} \
    EMAIL_RECIPIENT_NAME=${EMAIL_RECIPIENT_NAME} \
    EMAIL_RECIPIENT_ADDRESS=${EMAIL_RECIPIENT_ADDRESS}


VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp", "app:app/lib/*","com.mw.portfolio.PortfolioApplication", "-Xshareclasses", "-Xquickstart"]