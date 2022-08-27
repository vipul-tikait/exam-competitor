FROM openjdk:11

WORKDIR /exam-competitor

EXPOSE 18080

ARG JAR_FILE= ./target/exam-competitor-backend-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} /exam-competitor/exam-competitor-backend-0.0.1-SNAPSHOT.jar

ENTRYPOINT exec java -jar exam-competitor-backend-0.0.1-SNAPSHOT.jar