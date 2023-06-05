package pl.softwareskill.course.kafka.hwmonitor.alertconsumer;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import pl.softwareskill.course.kafka.hwmonitor.consumer.HardwareInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class KafkaConsumerRunnable implements Runnable {
    AtomicBoolean closed = new AtomicBoolean(false);
    KafkaConsumer consumer;
    String topicName;

    public KafkaConsumerRunnable(KafkaConsumer consumer, String topicName) {
        this.consumer = consumer;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList(topicName));
            while (!closed.get()) {
                ConsumerRecords<String, HardwareAlertInfo> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, HardwareAlertInfo> record : records) {
                    log.info("Alert! Free disk space is too low! hardware alert info: " + LocalDateTime.now() + " "
                            + record.value().toString());
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}