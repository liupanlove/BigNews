package bignews.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopupWindowActivity extends Activity
{
    PopupWindow pop;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

    }

}