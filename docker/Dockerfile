FROM java:8

ENV DEPLOY /opt/deploy

RUN mkdir $DEPLOY

ADD ./ex1-assembly-1.0.0.jar  $DEPLOY/run.jar

EXPOSE 8000

EXPOSE 9000

VOLUME $DEPLOY

WORKDIR $DEPLOY

ENTRYPOINT java -jar run.jar -http.port=:8000 -admin.port=:9000 -mysql.username=root -mysql.password=simonxikun -mysql.dbserver=mysql:3306 -mysql.dbname=alottodb