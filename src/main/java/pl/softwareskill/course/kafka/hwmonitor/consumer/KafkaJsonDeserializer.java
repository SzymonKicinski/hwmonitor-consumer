package pl.softwareskill.course.kafka.hwmonitor.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class KafkaJsonDeserializer<T> implements Deserializer {

    private Class<T> type;

    public KafkaJsonDeserializer(Class type) {
        this.type = type;
    }

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        T obj = null;
        try {
            obj = mapper.readValue(bytes, type);
        } catch (Exception e) {
            log.error("Error while deserialize message", e);
        }
        return obj;
    }

    @Override
    public void close() {

    }
}
