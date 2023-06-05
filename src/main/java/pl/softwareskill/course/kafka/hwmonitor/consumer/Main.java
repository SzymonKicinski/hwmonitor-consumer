package pl.softwareskill.course.kafka.hwmonitor.consumer;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Main {

    private final static String TOPIC_NAME = "cpuinfo";
    private final static String GROUP_ID = "group-1";
    private final static String BOOTSTRAP_SERVERS = "localhost:9091";

    public static void main(String[] args) {
        var consumerProperites = createConsumerProperites();

        KafkaConsumer<String, HardwareInfo> consumer = new KafkaConsumer<String, HardwareInfo>(
                consumerProperites,
                new StringDeserializer(),
                new KafkaJsonDeserializer<HardwareInfo>(HardwareInfo.class));

        Thread thread = new Thread(new KafkaConsumerRunnable(consumer, TOPIC_NAME));
        thread.start();
    }

    private static Properties createConsumerProperites() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return consumerProps;
    }
}