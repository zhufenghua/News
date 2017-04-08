package com.example.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.news.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 *  Fragmet需要添加xml布局文件
 */

@ContentView(value = R.layout.home_page)
public class HomePageFragment extends Fragment {


    @Nullable
    @Override  // 每个Fragment都有自己的XML配置文件
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("jxy",this.getClass() + "---->2: onCreateView");
        return x.view().inject(this,inflater,null);
//        return View.inflate(this.getActivity(), R.layout.home_page,null);
    }




}
