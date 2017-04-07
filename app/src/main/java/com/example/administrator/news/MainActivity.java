package com.example.administrator.news;

import android.app.Activity;
import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);  // 退出app 0 代表正常退出
    }
}
