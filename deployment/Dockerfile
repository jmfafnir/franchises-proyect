FROM eclipse-temurin:21-jdk-alpine
COPY *.jar franchises.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar franchises.jar" ]
