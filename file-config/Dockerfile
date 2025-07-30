# Dockerfile
FROM debezium/connect:2.5  # Adjust version as needed

ENV CONFLUENT_VERSION=7.6.0
ENV CONNECT_PLUGIN_DIR=/kafka/connect

# Download Avro converter and its dependencies
RUN curl -SL https://packages.confluent.io/maven/io/confluent/kafka-connect-avro-converter/${CONFLUENT_VERSION}/kafka-connect-avro-converter-${CONFLUENT_VERSION}.jar -o ${CONNECT_PLUGIN_DIR}/kafka-connect-avro-converter-${CONFLUENT_VERSION}.jar && \
    curl -SL https://packages.confluent.io/maven/io/confluent/kafka-schema-serializer/${CONFLUENT_VERSION}/kafka-schema-serializer-${CONFLUENT_VERSION}.jar -o ${CONNECT_PLUGIN_DIR}/kafka-schema-serializer-${CONFLUENT_VERSION}.jar && \
    curl -SL https://packages.confluent.io/maven/io/confluent/kafka-avro-serializer/${CONFLUENT_VERSION}/kafka-avro-serializer-${CONFLUENT_VERSION}.jar -o ${CONNECT_PLUGIN_DIR}/kafka-avro-serializer-${CONFLUENT_VERSION}.jar && \
    curl -SL https://packages.confluent.io/maven/io/confluent/common-config/${CONFLUENT_VERSION}/common-config-${CONFLUENT_VERSION}.jar -o ${CONNECT_PLUGIN_DIR}/common-config-${CONFLUENT_VERSION}.jar && \
    curl -SL https://packages.confluent.io/maven/io/confluent/common-utils/${CONFLUENT_VERSION}/common-utils-${CONFLUENT_VERSION}.jar -o ${CONNECT_PLUGIN_DIR}/common-utils-${CONFLUENT_VERSION}.jar
