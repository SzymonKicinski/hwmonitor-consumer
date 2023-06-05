package pl.softwareskill.course.kafka.hwmonitor.average;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import pl.softwareskill.course.kafka.hwmonitor.alertconsumer.KafkaJsonDeserializer;
import pl.softwareskill.course.kafka.hwmonitor.consumer.HardwareInfo;
import pl.softwareskill.course.kafka.hwmonitor.consumer.KafkaConsumerRunnable;

import java.util.Properties;

public class Main {

    private final static String TOPIC_NAME = "cpuinfo";
    private final static String GROUP_ID = "group-1";
    private final static String BOOTSTRAP_SERVERS = "localhost:9091";

    public static void main(String[] args) {
        var consumerProperites = createConsumerProperites();

        // cpuinfo
        KafkaConsumer<String, HardwareInfo> consumerAverage = new KafkaConsumer<String, HardwareInfo>(
                consumerProperites,
                new StringDeserializer(),
                new KafkaJsonDeserializer<HardwareInfo>(HardwareInfo.class));
        System.out.println("cpuinfo");
        Thread threadAverage = new Thread(new KafkaConsumerAverageRunnable(consumerAverage, TOPIC_NAME));
        threadAverage.start();

    }

    private static Properties createConsumerProperites() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return consumerProps;
    }
}