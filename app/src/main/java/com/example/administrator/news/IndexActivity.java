package com.example.administrator.news;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.utils.SharedPreUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_index)
public class IndexActivity extends Activity {
    @ViewInject(R.id.iv)
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_index);
        x.view().inject(this);
    }

    @Event(value = R.id.btn_http)
    private void http(View view) {
        Log.i("jxy", "abc............");
        RequestParams params = new RequestParams("http://www.jxy-edu.com/info.txt");
        params.addQueryStringParameter("name", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });

        Log.i("jxy", "显示图片........");


// 设置加载图片的参数
        ImageOptions options = new ImageOptions.Builder()
// 是否忽略GIF格式的图片
                .setIgnoreGif(false)
// 图片缩放模式
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
// 下载中显示的图片
                .setLoadingDrawableId(R.mipmap.index)
// 下载失败显示的图片
                .setFailureDrawableId(R.mipmap.index)
// 得到ImageOptions对象
                .build();
// 加载图片
        x.image().bind(iv, "http://www.jxy-edu.com/hehe.png", options, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable arg0) {
                Log.i("jxy", "onSuccess........");
            }

            @Override
            public void onFinished() {
                Log.i("jxy", "onFinished........");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {

                Log.i("jxy", "onError........");
            }

            @Override
            public void onCancelled(Callback.CancelledException arg0) {
                Log.i("jxy", "onCancelled........");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 此处可以判断用户是否开启网络, 或者启动Service下载网络数据,
        // 两秒后跳转到引导页面
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                // 默认跳转到欢迎页面,如果欢迎页面已显示,则直接跳转主页面
                if (SharedPreUtils.getBoolean(IndexActivity.this, "welcome_show", false)) {
                    Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                    // 标准模式在同一个APP中所有Activity都在同一个栈
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // 启动主页面
                    startActivity(intent);
                } else {
                    startActivity(new Intent(IndexActivity.this, WelcomeActivity.class));
                }
            }
        }, 20000);
    }
}
