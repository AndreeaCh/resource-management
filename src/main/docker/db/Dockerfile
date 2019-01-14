FROM mongo:4.0.5-xenial

ENV LANG=C.UTF-8 \
    SCRIPTS=/home/mongodb/scripts \
    IMPORT=/home/mongodb/import \
    LOGS=/home/mongodb/logs \
    MONGO_DATA=/data/db \
    MONGO_LOGS=/var/log

RUN mkdir -p ${SCRIPTS} ${IMPORT} ${LOGS} \
    && chown -R mongodb:mongodb ${SCRIPTS} ${IMPORT} ${LOGS} ${MONGO_DATA} ${MONGO_LOGS}

COPY fillDb/* ${IMPORT}/
COPY start-service.sh ${SCRIPTS}

RUN chown -R mongodb:mongodb ${SCRIPTS}/start-service.sh && chmod u+rx ${SCRIPTS}/start-service.sh

USER mongodb

WORKDIR ${SCRIPTS}
ENTRYPOINT ["./start-service.sh"]