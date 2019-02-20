FROM openjdk:8-jre-alpine

ENV LANG=C.UTF-8 SCRIPTS=/home/container/scripts BIN=/home/container/bin LOGS=/home/container/logs CFG=/home/container/config

RUN addgroup -S container && adduser -S container -g container \
    && mkdir ${SCRIPTS} ${BIN} ${LOGS} ${CFG} \
    && chown -R container:container ${SCRIPTS} ${BIN} ${LOGS}

COPY resource-management.jar ${BIN}/easy-manage.jar
COPY start-service.sh ${SCRIPTS}

RUN chown -R container:container ${SCRIPTS}/start-service.sh && chmod u+rx ${SCRIPTS}/start-service.sh \
&& chown -R container:container ${BIN}/easy-manage.jar

USER container

WORKDIR ${SCRIPTS}
ENTRYPOINT ["./start-service.sh"]
