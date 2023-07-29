FROM gamecore:latest

MAINTAINER Pseudow

WORKDIR /usr/minecraft/

ENV PROJECT_NAME=altarise-bedwars
ENV FOLDER=/usr/minecraft/plugins/

COPY src/main/resources/gameconfig plugins/Bedwars/gameconfig

RUN bash download_artifacts.sh -C

# Downloading dependencies....

RUN ls -C
RUN ls plugins/ -C

RUN echo Starting minecraft server...

ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:+DisableExplicitGC", "-jar", "paper.jar"]
