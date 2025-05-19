package orion.gz.scheduleview;

import android.graphics.Color;

import java.time.LocalTime;

public class ScheduleEventItem {
    public int id;
    public String name;
    public LocalTime startTime;
    public LocalTime endTime;
    public int color = Color.parseColor("#FF453A");

    public ScheduleEventItem(int id, String name, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleEventItem(int id, String name, LocalTime startTime, LocalTime endTime, int color) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}