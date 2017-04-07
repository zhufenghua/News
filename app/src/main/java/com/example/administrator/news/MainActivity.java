package com.example.administrator.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewInject(R.id.rgGroug)
    private RadioGroup radioGroup;
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        x.view().inject(this);
        // 默认配置被选中的单选按钮
        radioGroup.check(R.id.rbHome);
        initData();
    }

    // 进行组件的赋值操作
    private void initData(){
        // 给ViewPager添加数据,
        viewpager.setAdapter(new MainPage());
        // 给按钮组注册单击事件,让单击触发ViewPager页面切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override  // Called when the checked radio button has changed. When the selection is cleared, checkedId is -1.
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                   switch (checkedId){
                           case R.id.rbHome:
                               // 手动调用viewPager切换页面
                               viewpager.setCurrentItem(0,true);
                               break;
                           case R.id.rbTime:
                               viewpager.setCurrentItem(1,true);
                               break;
                   }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);  // 退出app 0 代表正常退出
    }

    private class MainPage extends PagerAdapter {

        List<View> iList=new ArrayList<View>();

        public MainPage() {
            iList.add(View.inflate(MainActivity.this,R.layout.home_page,null));
            iList.add(View.inflate(MainActivity.this,R.layout.time_page,null));
        }

        @Override  // 返回集合大小
        public int getCount() {
            return iList.size();
        }

        @Override // 实例化没一个Item,其实就是View
        public Object instantiateItem(ViewGroup container, int position) {
            View view = (View) iList.get(position);
            Log.i("jxy", "当前的ViewPager对象:" + container + ",position:" + position + ",ImageView:" + view);
            // 返回之前必须把当前View对象添加到容器中
            container.addView(view); // lv.addView(view); 只能使用适配器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("jxy", "当前销毁的对象:" + object);
            container.removeView((View) object);
        }

        @Override  //
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
