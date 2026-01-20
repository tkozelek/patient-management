FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests -DfinalName=app

FROM eclipse-temurin:21-jre AS runner

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

COPY --from=builder ./app/target/*.jar ./app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 4000

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]