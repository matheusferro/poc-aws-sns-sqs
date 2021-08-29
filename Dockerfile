FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=./build/libs/poc-aws-sns-sqs-*all.jar
COPY ${JAR_FILE} app.jar

EXPOSE 50051
ENTRYPOINT ["java", "-Xmx512m","-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "/app.jar"]