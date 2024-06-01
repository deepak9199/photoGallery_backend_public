FROM openjdk:11

COPY target/photogallery-0.0.1-SNAPSHOT.jar photogallery-0.0.1-SNAPSHOT.jar

RUN mkdir uploads && chmod 777 uploads

ENTRYPOINT [ "java","-jar","/photogallery-0.0.1-SNAPSHOT.jar" ]