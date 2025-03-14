# Sử dụng OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Cài đặt Maven
RUN apt-get update && apt-get install -y maven

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Biên dịch source code bằng Maven
RUN mvn clean package -DskipTests

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "target/*.jar"]
