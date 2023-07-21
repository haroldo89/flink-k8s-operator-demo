# Adding dependencies to the project #

### Open the pom.xml file in your project directory and add the dependency in between the dependencies tab.

For example, you can add the Kafka connector as a dependency and the configuration like this :

```
<properties>
    <flink.version>1.13.6</flink.version>
</properties>

<dependencies>

    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-java</artifactId>
      <version>${flink.version}</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-streaming-java_${scala.binary.version}</artifactId>
      <version>${flink.version}</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-clients_${scala.binary.version}</artifactId>
      <version>${flink.version}</version>
      <scope>compile</scope>
    </dependency>
    <!-- Dependencia de Kafka -->

    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-connector-kafka_2.11</artifactId>
      <version>${flink.version}</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-connector-base</artifactId>
      <version>${flink.version}</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
<build>
    <plugins>

      <!-- Java Compiler -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <configuration>
          <source>${target.java.version}</source>
          <target>${target.java.version}</target>
        </configuration>
      </plugin>

      <!-- We use the maven-shade plugin to create a fat jar that contains all necessary
			dependencies. -->
      <!-- Change the value of <mainClass>...</mainClass> if your program entry point changes. -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.1.1</version>
        <executions>
          <!-- Run shade goal on package phase -->
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <artifactSet>
                <excludes>
                  <exclude>org.apache.flink:force-shading</exclude>
                  <exclude>com.google.code.findbugs:jsr305</exclude>
                  <exclude>org.slf4j:*</exclude>
                  <exclude>org.apache.logging.log4j:*</exclude>
                </excludes>
              </artifactSet>
              <filters>
                <filter>
                  <!-- Do not copy the signatures in the META-INF folder.
									Otherwise, this might cause SecurityExceptions when using the JAR. -->
                  <artifact>*:*</artifact>
                  <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                  </excludes>
                </filter>
              </filters>
              <transformers>
                <transformer
                  implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <!-- Do not copy the signatures in the META-INF folder -->.
                  <mainClass>com.flink.KafkaConsumer</mainClass>
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
</build>
```
