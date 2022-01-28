FROM gradle:7-jdk17 as build
WORKDIR /app

COPY . .

RUN gradle build --no-daemon 

FROM  openjdk:17-jdk-alpine

ENV TZ=Europe/Prague
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY --from=build /app/build/libs/pia-socialnetwork-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java","-jar","app.jar"]