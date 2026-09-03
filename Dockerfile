# --- ЭТАП 1: Сборка приложения ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Копируем настройки сборщика и исходный код
COPY pom.xml .
COPY src ./src

# Собираем fat-JAR файл, пропуская тесты для скорости сборки образа
RUN mvn clean package -DskipTests

# --- ЭТАП 2: Запуск приложения ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Создаем безопасного системного пользователя, чтобы не запускать приложение от root (стандарт безопасности)
RUN addgroup -S techmatrix && adduser -S techmatrix -G techmatrix
USER techmatrix

# Копируем собранный JAR-файл из предыдущего этапа (имя строго по вашему pom.xml)
COPY --from=builder /app/target/java-project-ev-charging-network-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, на котором работает наш сервер из application.yml
EXPOSE 8080

# Оптимальные runtime-настройки JVM для контейнеров (активация cgroups лимитов памяти)
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]

