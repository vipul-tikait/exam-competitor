FROM openjdk:11
EXPOSE 8080
ADD target/uss-github.jar uss-github.jar
ENTRYPOINT ["java","-jar","/uss-github.jar"] 