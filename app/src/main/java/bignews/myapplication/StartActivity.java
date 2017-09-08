package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity
{
    private long delayTime = 2000;   // sleep 2s
    private boolean flag = false;
    Timer timer;
    TimerTask task;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                openMainActivity();
            }
        };
        timer.schedule(task, delayTime);

        TextView text = (TextView) findViewById(R.id.skip);      // skip
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
                timer.cancel();
            }
        });
    }

    private void openMainActivity()
    {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean onkeyDown(int keyCode, KeyEvent event)      // 屏蔽后退键   有问题
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}