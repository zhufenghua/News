package com.example.administrator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.model.News;
import com.example.administrator.model.Result;
import com.example.administrator.news.R;
import com.example.administrator.view.ReFreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragmet需要添加xml布局文件
 */
@ContentView(R.layout.time_page)
public class TimePageFragment extends Fragment {  // 不能单独使用,在运行的时候必须依附Activty

    @ViewInject(R.id.listview)  // 需要添加listItem
    private ReFreshListView listview;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("jxy", this.getClass() + "---->1: onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("jxy", this.getClass() + "---->2: onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("jxy", this.getClass() + "---->3: onCreateView");
        return x.view().inject(this, inflater, null);
    }

    @Override  // 布局创建完毕,一般此方法用来初始化数据
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("jxy", this.getClass() + "---->4: onViewCreated");
        // 给listView添加数据(创建适配器),不能直接添加,listview.addView();
        // 1: 此处应该先发送http请求,如果进入: onSuccess,则说明数据已经获取, 应该创建Service服务(此服务并不是用来播放音乐的,因此应该
        // bingService的方式,而非startService)
        getServiceData();

        listview.setRefreshListener(new ReFreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServiceData();  // 重新调用下拉刷新
            }
        });
    }

    public void getServiceData() {
        RequestParams params = new RequestParams("http://hiwbs101083.jsp.jspee.com.cn/NewsServlet");
        // 有搜索框的时候实现
//        params.addQueryStringParameter("wd", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                // 2: Gson 把string转化为model /map
                Gson gson = new Gson();
//                final List<News> newsList = gson.fromJson(result, new TypeToken<ArrayList<News>>() {
                //                }.getType());
                Result fromJson = gson.fromJson(result, Result.class);
                Log.i("jxy", "从后台返回的数据为:" + fromJson);
                // 设置最后刷新时间
                TextView txtLastTime = (TextView) listview.findViewById(R.id.txt_lastTime);
                txtLastTime.setText(fromJson.getSysTime());
                final List<News> newsList = fromJson.getNewsList();
                // 对数据进行赋值(List_Item的数据适配)
                listview.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return newsList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return newsList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;
                        if (convertView != null) { // 说明缓存中已存在List Item
                            view = convertView;
                        } else {
                            view = View.inflate(TimePageFragment.this.getActivity(), R.layout.time_list_item, null);
                        }
                        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
                        TextView txtTime = (TextView) view.findViewById(R.id.txt_time);
                        ImageView imgIv = (ImageView) view.findViewById(R.id.img_iv);
                        News news = (News) getItem(position);
                        txtTitle.setText(news.getTitle());
                        txtTime.setText(news.getDate());
                        // 设置加载图片的参数
                        ImageOptions options = new ImageOptions.Builder()
                                // 是否忽略GIF格式的图片
                                .setIgnoreGif(false)
                                // 图片缩放模式
                                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                                // 下载中显示的图片
                                .setLoadingDrawableId(R.drawable.time_loading)
                                // 支持缓存操作,默认true
                                .setUseMemCache(true)
                                // 下载失败显示的图片
                                .setFailureDrawableId(R.drawable.time_fail)
                                // 得到ImageOptions对象
                                .build();

                        // 通过xutils3 下载图片,并且支持缓存功能
                        x.image().bind(imgIv, news.getImgUrl(), options);
                        return view;
                    }
                });

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "onError" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override  // 只要请求完毕,无论成功还是失败,此方法都会执行
            public void onFinished() {
                // 下拉刷新完毕
                listview.endPulldownToRefresh();
            }

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("jxy", this.getClass() + "---->5: onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("jxy", this.getClass() + "---->6: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("jxy", this.getClass() + "---->7: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("jxy", this.getClass() + "---->8: onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("jxy", this.getClass() + "---->9: onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("jxy", this.getClass() + "---->10: onDestroy");
    }


}
