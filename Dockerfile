# ---- build stage: compile and package the jar ----
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /build
# pom first so the dependency layer is cached until the pom changes
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -DskipTests

# ---- runtime stage: JRE only, non-root ----
FROM eclipse-temurin:25-jre
WORKDIR /app
RUN useradd --system --no-create-home app
COPY --from=build /build/target/*.jar app.jar
USER app
ENTRYPOINT ["java", "-jar", "app.jar"]
