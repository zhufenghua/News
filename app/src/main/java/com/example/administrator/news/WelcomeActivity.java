package com.example.administrator.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 绑定组件
        initView();
        // 初始化数据
        initData();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initData() {
        viewpager.setAdapter(new WelcomePage());
    }

    private class WelcomePage extends PagerAdapter {

        private int[] ids = null;
        private List<ImageView> iList = null;

        public WelcomePage(){
            ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
            iList = new ArrayList<ImageView>();
            // 创建一个ListView<ImageView>来存储图片,
            for(int i=0;i<ids.length;i++){
                ImageView imageView = new ImageView(WelcomeActivity.this);
                // 给当前ImageView设置背景图
                imageView.setBackgroundResource(ids[i]);
                iList.add(imageView);
            }
        }

        @Override  // 返回集合大小
        public int getCount() {
            return iList.size();
        }

        @Override // 实例化没一个Item,其实就是View
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =  (ImageView)iList.get(position);
            Log.i("jxy","当前的ViewPager对象:" + container + ",position:" + position + ",ImageView:" + view);
            // 返回之前必须把当前View对象添加到容器中
            container.addView(view); // lv.addView(view); 只能使用适配器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("jxy","当前销毁的对象:" + object);
            container.removeView((ImageView)object);
        }

        @Override  //
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
