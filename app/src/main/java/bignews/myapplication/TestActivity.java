package bignews.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.zip.Inflater;

public class TestActivity extends AppCompatActivity{

    private PopupWindow mPopWindow;

        private SpeechSynthesizer mTts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        LinearLayout testLinearLayout = (LinearLayout) findViewById(R.id.testLinearLayout);
        Button button = new Button(this);
        button.setText("haipa");
        testLinearLayout.addView(button);
    }

    /*@Override
    public void onResume()
    {
        super.onResume();
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTts.startSpeaking("中华人民共和国，中华人民共和国，中华人民共和国，中华人民共和国，中华人民共和国，中华人民共和国，中华人民共和国", null);
            }
        });
    }*/
}
