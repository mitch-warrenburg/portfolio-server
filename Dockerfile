FROM adoptopenjdk:11-jdk-openj9 as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN ./gradlew dependencies --stacktrace

COPY src src

RUN ./gradlew build -x test --stacktrace
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM adoptopenjdk:11-jdk-openj9
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp", "app:app/lib/*","com.mw.portfolio.PortfolioApplication", "-Xshareclasses", "-Xquickstart"]