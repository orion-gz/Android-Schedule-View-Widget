package orion.gz.scheduleview;

import java.time.LocalTime;

public interface OnScheduleTouchListener {
    void onEventClick(ScheduleEventItem eventItem);
    void onEmptySlotClick(LocalTime time);
}
