package com.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;

public class App {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        int counter = 0;
        while (counter < 15) {
            Thread.sleep(1000);
            counter++; 
            // set up the streaming execution environment
            // Configure the data source: integer numbers in a stream
            DataStream<Integer> inputStream = env.fromElements(1, 2, 5, 8, 13, 21, 34, 55, 89);
            // Apply the keyBy operation to create a KeyedStream
            DataStream<Integer> keyedStream = inputStream.keyBy(value -> 0); // Key all elements to a single key
            // Perform the reduce operation on the KeyedStream to sum the integers
            DataStream<Integer> sumStream = ((KeyedStream<Integer, Integer>) keyedStream).reduce(new SumReducer());
            // Print the result of the sum
            sumStream.print();
        }
        // Execute the Flink application
        env.execute("SumIntegerApp");
    }

    // Custom ReduceFunction for summing integers
    private static class SumReducer implements ReduceFunction<Integer> {
        @Override
        public Integer reduce(Integer value1, Integer value2) {
            return value1 + value2;
        }
    }
}
