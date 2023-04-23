FROM arm64v8/tomee:9.0.0-jre17-microprofile
RUN apt update && \
    apt install unzip && \
    curl -LO https://search.maven.org/remotecontent?filepath=org/jacoco/jacoco/0.8.9/jacoco-0.8.9.zip && \
    unzip jacoco-*.zip lib/jacocoagent.jar
RUN rm -rf /usr/local/tomee/webapps/*
ADD https://jdbc.postgresql.org/download/postgresql-42.6.0.jar lib/
COPY docker/backend/server.xml \
     docker/backend/tomee.xml \
     docker/backend/localhost-rsa.pfx \
     docker/backend/localhost-rsa.cert \
     /usr/local/tomee/conf/
COPY app/app/build/libs/app.war /usr/local/tomee/webapps/ROOT.war
