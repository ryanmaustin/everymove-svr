FROM openjdk:latest
VOLUME /tmp
COPY build/libs/*.jar app.jar
COPY build/stockfish /usr/bin/stockfish
ENTRYPOINT ["java","-jar","/app.jar"]
