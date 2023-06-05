package pl.softwareskill.course.kafka.hwmonitor.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import static lombok.AccessLevel.PRIVATE;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;

@FieldDefaults(level = PRIVATE)
@Builder
@Data
public class HardwareInfo {

    Long availableMemory;
    Long discFreeSpace;

    HardwareInfo(@JsonProperty("availableMemory") Long availableMemory,
                 @JsonProperty("discFreeSpace") Long discFreeSpace) {
        this.availableMemory = availableMemory;
        this.discFreeSpace = discFreeSpace;
    }

    @Override
    public String toString() {
        return "Available memory: " + FileUtils.byteCountToDisplaySize(this.availableMemory) +
                "Disc free space: " + FileUtils.byteCountToDisplaySize(this.discFreeSpace);
    }
}
