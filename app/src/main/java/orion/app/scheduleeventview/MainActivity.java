package orion.app.scheduleeventview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import orion.gz.scheduleview.OnScheduleTouchListener;
import orion.gz.scheduleview.ScheduleEventItem;
import orion.gz.scheduleview.ScheduleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<ScheduleEventItem> list = Arrays.asList(
                new ScheduleEventItem(1, "모바일 프로그래밍", LocalTime.of(10, 0), LocalTime.of(12, 0), Color.parseColor("#dd3333")),
                new ScheduleEventItem(2, "인공지능 개론", LocalTime.of(12, 30), LocalTime.of(14, 30), Color.parseColor("#1e73be")),
                new ScheduleEventItem(3, "AI 수학", LocalTime.of(15, 0), LocalTime.of(17, 0), Color.parseColor("#eeee22")),
                new ScheduleEventItem(4, "데이터 과학", LocalTime.of(17, 30), LocalTime.of(18, 00), Color.parseColor("#8224e3")),
                new ScheduleEventItem(5, "디지털 마케팅", LocalTime.of(23, 50), LocalTime.of(23, 59), Color.parseColor("#e12797")),
                new ScheduleEventItem(6, "운영체제", LocalTime.of(17, 10), LocalTime.of(17, 20), Color.parseColor("#e12797"))
        );

        ScheduleView scheduleView = findViewById(R.id.schedule_view);
        scheduleView.setScheduleEventItems(list);

        scheduleView.setOnScheduleTouchListener(new OnScheduleTouchListener() {
            @Override
            public void onEventClick(ScheduleEventItem eventItem) {
                Toast.makeText(MainActivity.this, eventItem.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmptySlotClick(LocalTime time) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    String formattedTime = time.format(formatter);

                    Log.d("Schedule", formattedTime);
                    scheduleView.addScheduleEventItem(new ScheduleEventItem(6, "운영 체제", time, time.plusHours(1)));
                }
            }
        });
    }
}