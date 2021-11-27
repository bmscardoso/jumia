#FROM java:8 AS base
FROM goyalzz/ubuntu-java-8-maven-docker-image

CMD sudo apt-get install sqlite3-pcre

COPY target/brunocardoso-1.0.0.jar /com/jumia/brunocardoso/webapp/brunocardoso-1.0.0.jar
COPY sample.db /sample.db

CMD java -jar /com/jumia/brunocardoso/webapp/brunocardoso-1.0.0.jar
