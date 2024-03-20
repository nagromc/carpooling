FROM maven:3-openjdk-17-slim AS dependency-cache

WORKDIR /usr/src/app
COPY pom.xml .
COPY carpooling-domain/pom.xml carpooling-domain/pom.xml
COPY carpooling-json-database/pom.xml carpooling-json-database/pom.xml
COPY carpooling-rest/pom.xml carpooling-rest/pom.xml
COPY carpooling-app/pom.xml carpooling-app/pom.xml
RUN mvn \
    --batch-mode \
    --errors \
    --strict-checksums \
    --settings /usr/share/maven/ref/settings-docker.xml \
    compile \
    org.apache.maven.plugins:maven-dependency-plugin:3.2.0:resolve-plugins \
    org.apache.maven.plugins:maven-dependency-plugin:3.2.0:go-offline

FROM maven:3-openjdk-17-slim AS builder

WORKDIR /usr/src/app
COPY . .
RUN mvn \
    --batch-mode \
    --errors \
    --activate-profiles disable-toolchains \
    --settings /usr/share/maven/ref/settings-docker.xml \
    clean package

FROM openjdk:17-slim AS runner

WORKDIR /app
COPY --from=builder /usr/src/app/carpooling-app/target/carpooling-app.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/carpooling-app.jar"]
