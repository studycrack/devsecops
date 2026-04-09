FROM openjdk:11

RUN apt update -y; apt install -y supervisor

RUN mkdir -p /app

WORKDIR /app

COPY vsnippet .
COPY config/supervisord.conf /etc/supervisord.conf

EXPOSE 8080

ENTRYPOINT [ "/usr/bin/supervisord", "-c", "/etc/supervisord.conf" ]
