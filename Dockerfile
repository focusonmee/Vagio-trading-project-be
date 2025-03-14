# Sử dụng OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file cấu hình Maven và source code vào container
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src src

# Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw

# Biên dịch source code bằng Maven
RUN ./mvnw clean package -DskipTests

# Copy file JAR đã build sang thư mục chạy ứng dụng
RUN cp target/*.jar app.jar

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]
