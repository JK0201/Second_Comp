# Build Phase
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

COPY . /app

RUN gradle clean build --no-daemon

# Run Phase
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/firstcomp.jar

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "firstcomp.jar"]