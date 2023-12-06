FROM openjdk:17.0.1-jdk-slim-bullseye as BUILDER

WORKDIR /tmp/build

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
COPY src ./src
ARG MVNARGS="package"

RUN ./mvnw -B ${MVNARGS} -Dmaven.test.skip -Dnet.bytebuddy.experimental=true



FROM openjdk:17.0.1-jdk-bullseye as RUNNER


RUN apt-get update -y \
    && apt-get install -y locales curl \
    && rm -rf /var/lib/apt/lists \
    && echo "pt_BR.UTF-8 UTF-8" > /etc/locale.gen \
    && locale-gen \
    && echo "LANG=\"pt_BR.UTF-8\"" > /etc/locale.conf \
    && rm -f /etc/localtime /etc/timezone \
    && ln -s /usr/share/zoneinfo/America/Maceio /etc/localtime \
    && ln -s /usr/share/zoneinfo/America/Maceio /etc/timezone

ENV LANG='pt_BR.UTF-8' LANGUAGE='pt_BR.UTF-8' LC_ALL='pt_BR.UTF-8'

COPY --from=BUILDER /tmp/build/target/*.war /api.jar

ENV SPRING_PROFILES_ACTIVE="local"
ENV SERVER_SERVLET_CONTEXT_PATH="/"

EXPOSE 8080

HEALTHCHECK --start-period=10s --interval=15s --timeout=3s --retries=3 \
	CMD curl --fail "http://localhost:8080${SERVER_SERVLET_CONTEXT_PATH}" || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/api.jar"]
