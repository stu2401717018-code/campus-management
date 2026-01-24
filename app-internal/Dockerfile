ARG JAVA_VERSION=17

FROM campus/base:1.0 as builder

ARG MAVEN_VERSION=3.9.6
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=$PATH:$MAVEN_HOME/bin

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM campus/base:1.0

ENV SERVER_PORT=8081
ENV SPRING_PROFILES_ACTIVE=docker
ENV LOG_PATH=/var/log/app

WORKDIR /app

RUN mkdir -p ${LOG_PATH} && chown -R appuser:appgroup ${LOG_PATH}

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:${SERVER_PORT}/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
