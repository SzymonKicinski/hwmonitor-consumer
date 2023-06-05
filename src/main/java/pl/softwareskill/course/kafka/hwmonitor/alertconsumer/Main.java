package pl.softwareskill.course.kafka.hwmonitor.alertconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class Main {

    private final static String TOPIC_NAME = "cpualert";
    private final static String GROUP_ID = "group-1";
    private final static String BOOTSTRAP_SERVERS = "localhost:9091";

    public static void main(String[] args) {
        var consumerProperites = createConsumerProperites();

        KafkaConsumer<String, HardwareAlertInfo> consumer = new KafkaConsumer<String, HardwareAlertInfo>(
                consumerProperites,
                new StringDeserializer(),
                new KafkaJsonDeserializer<HardwareAlertInfo>(HardwareAlertInfo.class));

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