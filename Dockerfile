# Build stage
FROM eclipse-temurin:21-jdk-alpine-3.23 AS build
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src/ src
RUN ./mvnw clean install

# Final stage
FROM eclipse-temurin:21-jre-alpine-3.23 AS final

WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /opt/app/voucherapp.jar

# extra utils
RUN apk add sqlite
# To remove hardcoded dev later on! 
ENTRYPOINT [ "java", "-jar", "/opt/app/voucherapp.jar" ]
# CMD [ "--spring.profiles.active=dev" ]



