# Sử dụng OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Biên dịch source code (nếu dùng Maven)
RUN ./mvnw clean package -DskipTests

# Nếu dùng Gradle, thay thế bằng:
# RUN ./gradlew build -x test

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "target/*.jar"]
