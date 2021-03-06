package iss.workshop.sa4108memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerTesting extends AppCompatActivity {

    TextView text, text2, text3;
    long starttime = 0;

    //this  posts a message to the main thread from our timertask
    //and updates the textfield
    final Handler h = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds     = seconds % 60;

            text.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            return false;
        }
    });

    //runs without timer be reposting self
    Handler h2 = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds     = seconds % 60;

            text.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

            h2.postDelayed(this, 500);
        }
    };

    //tells handler to send a message
    class firstTask extends TimerTask {

        @Override
        public void run() {
            h.sendEmptyMessage(0);
        }
    };

    //tells activity to run on ui thread
    class secondTask extends TimerTask {

        @Override
        public void run() {
            TimerTesting.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    long millis = System.currentTimeMillis() - starttime;
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    int hours = minutes / 60;
                    seconds     = seconds % 60;

                    text.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }
            });
        }
    };

    Timer timer = new Timer();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_testing);

        text = (TextView)findViewById(R.id.text);
        //text2 = (TextView)findViewById(R.id.text1);
        //text3 = (TextView)findViewById(R.id.text2);

        Button b = (Button)findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if(b.getText().equals("stop")){
                    timer.cancel();
                    timer.purge();
                    h2.removeCallbacks(run);
                    //b.setText("start");
                    Intent intent = new Intent(TimerTesting.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    starttime = System.currentTimeMillis();
                    timer = new Timer();
                    //timer.schedule(new firstTask(), 0,500);
                    timer.schedule(new secondTask(),  0,500);
                    h2.postDelayed(run, 0);
                    b.setText("stop");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        h2.removeCallbacks(run);
        Button b = (Button)findViewById(R.id.button);
        b.setText("start");
    }
}