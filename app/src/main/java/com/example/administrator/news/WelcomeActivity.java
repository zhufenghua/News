package com.example.administrator.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    private ViewPager viewpager;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 绑定组件
        initView();
        // 初始化数据
        initData();
        Log.i("jxy",this.getResources().getDisplayMetrics().density + "");  // 3.0
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll = (LinearLayout) findViewById(R.id.ll);
    }

    private void initData() {
        // 给ViewPager适配Item一般都ImageView
        viewpager.setAdapter(new WelcomePage());
        // 动态给线性 布局添加三个小灰点
        for (int i = 0; i < 3; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.welcome_point_gray);
            // 代码中所有的数字的单位都是像素 px
            int px = DensityUtils.dpi2px(this,10);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(px,px);
            if(i>0){
                param.leftMargin = DensityUtils.dpi2px(this,5); // 20px
            }
            view.setLayoutParams(param);
            ll.addView(view);
        }
    }

    private class WelcomePage extends PagerAdapter {

        private int[] ids = null;
        private List<ImageView> iList = null;

        public WelcomePage() {
            ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
            iList = new ArrayList<ImageView>();
            // 创建一个ListView<ImageView>来存储图片,
            for (int i = 0; i < ids.length; i++) {
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
            ImageView view = (ImageView) iList.get(position);
            Log.i("jxy", "当前的ViewPager对象:" + container + ",position:" + position + ",ImageView:" + view);
            // 返回之前必须把当前View对象添加到容器中
            container.addView(view); // lv.addView(view); 只能使用适配器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("jxy", "当前销毁的对象:" + object);
            container.removeView((ImageView) object);
        }

        @Override  //
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
