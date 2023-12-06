FROM openjdk:17.0.1-jdk-slim-bullseye as BUILDER

WORKDIR /tmp/build

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
COPY src ./src
ARG MVNARGS="package"

RUN ./mvnw -B ${MVNARGS} -Dmaven.test.skip -Dnet.bytebuddy.experimental=true


FROM openjdk:17.0.1-jdk-slim-bullseye as RUNNER


RUN echo "America/Sao_Paulo" > /etc/timezone

ENV LANG='pt_BR.UTF-8' LANGUAGE='pt_BR.UTF-8' LC_ALL='pt_BR.UTF-8'

COPY --from=BUILDER /tmp/build/target/*.war /api.jar

ENV SPRING_PROFILES_ACTIVE="local"
ENV SERVER_SERVLET_CONTEXT_PATH="/"

EXPOSE 8080

HEALTHCHECK --start-period=10s --interval=15s --timeout=3s --retries=3 \
	CMD curl --fail "http://localhost:8080${SERVER_SERVLET_CONTEXT_PATH}" || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/api.jar"]
