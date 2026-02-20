FROM amazoncorretto:17

WORKDIR /app

COPY . .

RUN ./gradlew build

EXPOSE 80

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
