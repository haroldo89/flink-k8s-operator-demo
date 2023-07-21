FROM flink:1.13

RUN mkdir -p /opt/flink/demo/streaming
ADD target/flinkdemo*.jar /opt/flink/demo/streaming/flinkdemo.jar
