FROM arm64v8/tomee:9.0.0-jre17-microprofile
RUN rm -rf /usr/local/tomee/webapps/*
ADD https://jdbc.postgresql.org/download/postgresql-42.6.0.jar lib/
COPY docker/backend/server.xml \
     docker/backend/tomee.xml \
     docker/backend/localhost-rsa.pfx \
     docker/backend/localhost-rsa.cert \
     /usr/local/tomee/conf/
COPY app/app/build/libs/app.war /usr/local/tomee/webapps/ROOT.war
