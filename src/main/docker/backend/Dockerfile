FROM openjdk:8-jre-alpine

ENV LANG=C.UTF-8 SCRIPTS=/home/container/scripts BIN=/home/container/bin LOGS=/home/container/logs

RUN addgroup -S container && adduser -S container -g container \
    && mkdir ${SCRIPTS} ${BIN} ${LOGS} \
    && chown -R container:container ${SCRIPTS} ${BIN} ${LOGS}

COPY easy-manage.jar ${BIN}
COPY start-service.sh ${SCRIPTS}

RUN chown -R container:container ${SCRIPTS}/start-service.sh && chmod u+rx ${SCRIPTS}/start-service.sh \
&& chown -R container:container ${BIN}/easy-manage.jar

USER container

WORKDIR ${SCRIPTS}
ENTRYPOINT ["./start-service.sh"]
