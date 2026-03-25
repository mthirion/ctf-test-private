FROM quay.io/rhn-gps-mthirion/ctf-openjdk:1.21
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'
ARG KEY="l34ky_m3t4d4t4_v14_g1t0ps"
# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV AB_JOLOKIA_OFF=""
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
RUN echo "ctf.api-key=${KEY}" > /deployments/application-internal.prop
