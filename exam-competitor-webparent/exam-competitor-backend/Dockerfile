FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} uss-github.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/uss-github.jar"] 