package pl.softwareskill.course.kafka.hwmonitor.average;

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
@FieldDefaults(level = PRIVATE)
public class KafkaConsumerAverageRunnable implements Runnable {
    AtomicBoolean closed = new AtomicBoolean(false);
    KafkaConsumer consumer;
    String topicName;
    long availableMemoryAvarge;
    long discFreeSpaceAverge;
    Integer counter;

    public KafkaConsumerAverageRunnable(KafkaConsumer consumer, String topicName) {
        this.consumer = consumer;
        this.topicName = topicName;
        counter = 1;
        availableMemoryAvarge = 0;
        discFreeSpaceAverge = 0;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList(topicName));
            while (!closed.get()) {
                ConsumerRecords<String, HardwareInfo> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, HardwareInfo> record : records) {
                    // Suma
                    this.discFreeSpaceAverge =this.discFreeSpaceAverge +record.value().getDiscFreeSpace();
                    this.availableMemoryAvarge=this.availableMemoryAvarge+record.value().getAvailableMemory();
                    log.info("Hardware average info: " + LocalDateTime.now() + " " +
                            " disc free space average: " + this.discFreeSpaceAverge/this.counter + " "
                            + "available memory averge: " + this.availableMemoryAvarge/this.counter );
                    this.counter++;
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