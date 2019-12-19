FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER 1398729606@qq.com

RUN mkdir -p /galaxy/personnel

WORKDIR /galaxy/personnel

EXPOSE 8106

ADD ./target/personnel-management.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
