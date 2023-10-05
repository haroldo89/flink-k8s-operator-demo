package com.flink;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

public class KafkaConsumer {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                // .setBootstrapServers("broker:29092")
                .setBootstrapServers("axpreecgpk2lb.ecommerce.inditex.grp:9092")
                // .setTopics("example-topic")
                // .setTopics("meccano.global.pre.apps.all.private.flink-test.v2")
                .setTopics("meccano.global.pre.apps.all.private.knative-test.v2")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> messageStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        messageStream.print();

        env.execute("Flink streaming from Kafka");
    }
}
