package pl.softwareskill.course.kafka.hwmonitor.alertconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Builder
@Data
public class HardwareAlertInfo {

    Long availableRAMMemory;
    Long discFreeSpace;
    Long discTotalSpace;

    HardwareAlertInfo(@JsonProperty("availableRAMMemory") Long availableRAMMemory,
                      @JsonProperty("discFreeSpace") Long discFreeSpace,
                      @JsonProperty("discAllSpace") Long discTotalSpace) {
        this.availableRAMMemory = availableRAMMemory;
        this.discFreeSpace = discFreeSpace;
        this.discTotalSpace = discTotalSpace;
    }

    @Override
    public String toString() {
        String string = "";
        if (discFreeSpace < 57541340774.40) {
            string = " !Disc free space: " + FileUtils.byteCountToDisplaySize(this.discFreeSpace) + " is"
                    + " to low! Please consider extend a space disk to kept working server" + " Total size of disk is: " +
                    FileUtils.byteCountToDisplaySize(this.discTotalSpace);
//            System.out.println(FileUtils.byteCountToDisplaySize(this.discTotalSpace)
//                    + " vs " + FileUtils.byteCountToDisplaySize(this.discFreeSpace));
        } else {
            string = "Disk is in good shape. Value: " + FileUtils.byteCountToDisplaySize(this.discFreeSpace);
        }
        return string;
    }
}
