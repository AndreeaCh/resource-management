FROM node:10.15.0-alpine

ENV LANG=C.UTF-8 SCRIPTS=/home/container/scripts BIN=/home/container/bin LOGS=/home/container/logs

RUN addgroup -S container && adduser -S container -g container \
    && mkdir ${SCRIPTS} ${BIN} ${LOGS} \
    && chown -R container:container ${SCRIPTS} ${BIN} ${LOGS} \
    && npm install -g http-server

ADD resource-management-client-bin.tar.gz ${BIN}
COPY start-service.sh ${SCRIPTS}

RUN chown -R container:container ${SCRIPTS}/start-service.sh ${BIN}/dist \
&& chmod u+rx ${SCRIPTS}/start-service.sh

USER container

WORKDIR ${SCRIPTS}
ENTRYPOINT ["./start-service.sh"]
