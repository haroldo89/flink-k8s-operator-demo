FROM flink:1.17

RUN mkdir -p /tmp/flink
RUN mkdir -p /opt/flink/demo/streaming
ADD target/flinkdemo*.jar /opt/flink/demo/streaming/flinkdemo.jar

RUN chown -R flink:flink /tmp/flink 
RUN chown -R flink:flink /opt/flink/demo/streaming