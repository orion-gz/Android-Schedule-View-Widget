package orion.gz.scheduleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.timepicker.TimeFormat;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleView extends View {
    private List<ScheduleEventItem> scheduleEventItems = new ArrayList<>();
    private List<RectF> itemRects = new ArrayList<>();
    private Paint timePaint;
    private Paint eventPaint;
    private Paint textPaint;
    private boolean isCollapsing = false;
    private boolean showDuration = false;
    private int eventTextSize = 40;
    private int eventTextColor = Color.BLACK;
    private int timeTextSize = 36;
    private int timeTextColor = Color.BLACK;
    private int defaultColor = Color.parseColor("#BBD5EB");
    private int hourHeight = 200;
    private int labelWidth = 200;
    private int clockFormat = TimeFormat.CLOCK_12H;
    private LocalTime startTime = LocalTime.of(0, 0);
    private LocalTime endTime = LocalTime.of(23, 59);

    private OnScheduleTouchListener touchListener;

    public ScheduleView(Context context) {
        super(context);
        init(null);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.ScheduleView);
        eventTextSize = attr.getInt(R.styleable.ScheduleView_event_text_size, 40);
        eventTextColor = attr.getColor(R.styleable.ScheduleView_event_text_color, Color.BLACK);
        timeTextSize = attr.getInt(R.styleable.ScheduleView_time_text_size, 36);
        timeTextColor = attr.getColor(R.styleable.ScheduleView_time_text_color, Color.BLACK);
        showDuration = attr.getBoolean(R.styleable.ScheduleView_show_duration, false);
        isCollapsing = attr.getBoolean(R.styleable.ScheduleView_is_collapsing, false);

        timePaint = new Paint();
        timePaint.setColor(timeTextColor);
        timePaint.setTextSize(timeTextSize);
        timePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(eventTextColor);
        textPaint.setTextSize(eventTextSize);
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);

        eventPaint = new Paint();
        eventPaint.setColor(defaultColor);
        eventPaint.setAntiAlias(true);
    }

    public void addScheduleEventItem(ScheduleEventItem eventItem) {
        scheduleEventItems.add(eventItem);
        invalidate();
    }

    public void clearScheduleEventItems() {
        this.itemRects.clear();
        this.scheduleEventItems.clear();
        invalidate();
    }

    public void setScheduleEventItems(List<ScheduleEventItem> scheduleEventItems) {
        clearScheduleEventItems();
        this.scheduleEventItems = new ArrayList<>(scheduleEventItems);
        invalidate();
    }

    public void setOnScheduleTouchListener(OnScheduleTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public boolean isCollapsing() {
        return isCollapsing;
    }

    public void setCollapsing(boolean collapsing) {
        isCollapsing = collapsing;
    }

    public boolean isShowDuration() {
        return showDuration;
    }

    public void setShowDuration(boolean showDuration) {
        this.showDuration = showDuration;
    }

    public int getEventTextSize() {
        return eventTextSize;
    }

    public void setEventTextSize(int eventTextSize) {
        this.eventTextSize = eventTextSize;
    }

    public int getEventTextColor() {
        return eventTextColor;
    }

    public void setEventTextColor(int eventTextColor) {
        this.eventTextColor = eventTextColor;
    }

    public int getTimeTextSize() {
        return timeTextSize;
    }

    public void setTimeTextSize(int timeTextSize) {
        this.timeTextSize = timeTextSize;
    }

    public int getTimeTextColor() {
        return timeTextColor;
    }

    public void setTimeTextColor(int timeTextColor) {
        this.timeTextColor = timeTextColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public int getClockFormat() {
        return clockFormat;
    }

    public void setClockFormat(int clockFormat) {
        this.clockFormat = clockFormat;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchListener != null) {
            float x = event.getX();
            float y = event.getY() - 50;

            for (int i = 0; i < itemRects.size(); i++) {
                RectF rect = itemRects.get(i);
                if (rect.contains(x, y)) {
                    touchListener.onEventClick(scheduleEventItems.get(i));
                    return true;
                }
            }
            Log.d("Schedule", "x: " + x + ", y: " + y);
            LocalTime clickedTime = yToTime(y);
            touchListener.onEmptySlotClick(clickedTime);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public float getEventTopY(LocalTime time) {
        return timeToY(time);
    }

    private float timeToY(LocalTime time) {
        Duration duration = Duration.between(startTime, time);
        return duration.toMinutes() * hourHeight / 60F;
    }

    private LocalTime yToTime(float y) {
        Duration duration = Duration.ofMinutes((long) (y / hourHeight * 60F));
        return startTime.plus(duration);
    }

    private void drawTimeLabel(Canvas canvas, int topLimit, int bottomLimit) {
        String label;
        for (int h = 0; h < 24; h++) {
            int top = h * hourHeight;
            if (top < topLimit || top > bottomLimit) continue;

            if (clockFormat == TimeFormat.CLOCK_12H)
                label = (h < 12 ? "오전" : "오후") + " " + (h % 12 == 0 ? 12 : h % 12) + "시";
            else
                label = String.format("%02d", h) + "시";

            canvas.drawText(label, 20, top + 50, timePaint);
            canvas.drawLine(labelWidth, top + 35, getWidth(), top + 35, timePaint);
        }
        canvas.drawLine(labelWidth, topLimit, labelWidth, bottomLimit, timePaint);
    }

    private void drawScheduleEventItems(Canvas canvas, int topLimit, int bottomLimit) {
        for (ScheduleEventItem eventItem : scheduleEventItems) {
            float topY = timeToY(eventItem.startTime);
            float bottomY = timeToY(eventItem.endTime);

            if (topY < topLimit || bottomY > bottomLimit) {
                RectF rect = new RectF(labelWidth + 25, bottomLimit - 75, getWidth() - 20, bottomLimit);
                itemRects.add(rect);
                eventPaint.setColor(eventItem.getColor());

                canvas.drawRoundRect(rect, 20, 20, eventPaint);
                canvas.drawText(eventItem.name, rect.left + 40, rect.top + 55, textPaint);
                continue;
            }

            Log.d("Schedule", eventItem.getName() + " topY: " + topY + ", bottomY: " + bottomY);

            RectF rect = new RectF(labelWidth + 25, topY + 35, getWidth() - 20, bottomY + 35);

            itemRects.add(rect);
            eventPaint.setColor(eventItem.getColor());

            canvas.drawRoundRect(rect, 20, 20, eventPaint);

            if (bottomY - topY >= 100) {
                canvas.drawText(eventItem.name, rect.left + 40, rect.top + 70, textPaint);
                if (showDuration) {
                    Duration duration = Duration.between(eventItem.startTime, eventItem.endTime);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() - hours * 60;
                    String durationString = "";

                    if (hours > 0 && minutes > 0) {
                        durationString += String.valueOf(hours) + "시간 " + String.valueOf(minutes) + "분";
                        canvas.drawText(durationString, rect.right - 215, rect.bottom - 40, textPaint);
                    }
                    if (minutes > 0 && hours == 0) {
                        durationString += String.valueOf(minutes) + "분";
                        canvas.drawText(durationString, rect.right - 135, rect.bottom - 40, textPaint);
                    }
                    else if (hours > 0 && minutes == 0) {
                        durationString += String.valueOf(hours) + "시간";
                        canvas.drawText(durationString, rect.right - 135, rect.bottom - 40, textPaint);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHeight = (int) Duration.between(startTime, endTime).toMinutes() * hourHeight / 60;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, totalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect visibleRect = canvas.getClipBounds();
        int topLimit = visibleRect.top;
        int bottomLimit = visibleRect.bottom;

        drawTimeLabel(canvas, topLimit, bottomLimit);
        drawScheduleEventItems(canvas, topLimit, bottomLimit);

        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            params.height = (int) Duration.between(startTime, endTime).toMinutes() * hourHeight  / 60;
            setLayoutParams(params);
        }
    }
}
