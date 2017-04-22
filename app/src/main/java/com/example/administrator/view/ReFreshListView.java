package com.example.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.news.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 重写ListView让它支持下拉刷新
 */
public class ReFreshListView extends ListView {

    // 给下拉刷新定义几种状态. 当鼠标司松开时根据不同的状态执行不同的操作
    public static final int STATE_PULL_REFRESH = 0;   // 下拉刷新
    public static final int STATE_RELEASE_REFRESH = 1;   // 松开刷新
    public static final int STATE_REFRESHING = 2;   // 立即刷新(要去访问后台)

    private static int mCurrentState = STATE_PULL_REFRESH;

    @ViewInject(R.id.txt_pull)
    private TextView txtPull;
    @ViewInject(R.id.pb)
    private ProgressBar pb;
    @ViewInject(R.id.iv)
    private ImageView iv;

    public ReFreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("jxy", "ReFreshListView(Context context, AttributeSet attrs)");
// 添加头布局文件
        headView = View.inflate(this.getContext(), R.layout.time_listview_head, null);
// 设置headView的padding,此时需要得到当前headView的真实高度
        headView.measure(0, 0);
        headHeight = headView.getMeasuredHeight();
        headView.setPadding(0, -headHeight, 0, 0);
// android:visibility="gone" 此方法不会占据空间,但是不能在根标签中使用
        this.addHeaderView(headView);
        // 当前headView已经加载listView,因此可以进行属性与事件注入
        x.view().inject(this);

    }

    private int headHeight = 0;

    private View headView = null;

    private int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //  存储第一次获取的getRawY的值
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("jxy", this.getClass() + "MotionEvent.ACTION_DOWN");
                // 先获取初始的y轴
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("jxy", this.getClass() + "MotionEvent.ACTION_MOVE");
                int endY = (int) ev.getRawY();
                int dy = endY - startY;
                // 如果dy>0 则说明是向下滑动
                if (dy > 0) {
                    int paddingY = dy - headHeight;
                    headView.setPadding(0, paddingY, 0, 0);
                    //  paddingY > 0  当前文字显示松开刷新, <0: 文字显示下拉刷新
                    if (paddingY > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        mCurrentState = STATE_RELEASE_REFRESH;
                        reFreshState();
                    } else if (paddingY <= 0 && mCurrentState != STATE_PULL_REFRESH) {
                        mCurrentState = STATE_PULL_REFRESH;
                        reFreshState();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
//                startY = 0; // 还原初始值
                Log.i("jxy", this.getClass() + "MotionEvent.ACTION_UP");
                if (mCurrentState == STATE_PULL_REFRESH) {
                    // 客户已经取消下拉刷新,head直接隐藏即可
                    headView.setPadding(0, -headHeight, 0, 0);
                } else if (mCurrentState == STATE_RELEASE_REFRESH) {
                    // 修改成立即刷新,并且去后台服务器获取数据
                    mCurrentState = STATE_REFRESHING;  // 正在加载......
                    headView.setPadding(0, 0, 0, 0);
// 加载后台的数据
                    if (refreshListener != null) {
                        refreshListener.onRefresh();
                    }
                }
                reFreshState();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private OnRefreshListener refreshListener;

    // 使用者通过set方法吧此接口注入
    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

// 结束下拉刷新的方法,此方法在远程访问之后,无论http成功还是失败都会执行
public void endPulldownToRefresh() {
    iv.setVisibility(VISIBLE);
    pb.setVisibility(INVISIBLE);
    txtPull.setText("下拉刷新");
    headView.setPadding(0, -headHeight, 0, 0);
}

    public interface OnRefreshListener {
        // 刷新的时候调用的方法
        public void onRefresh();
    }

    // 根据不同的状态,修改下拉刷新的文本与图片值
    private void reFreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:  // 进度条隐藏,箭头显示, 文字为"下拉刷新"
                pb.setVisibility(INVISIBLE);
                iv.setVisibility(VISIBLE);
                txtPull.setText("下拉刷新");
                break;
            case STATE_RELEASE_REFRESH:
                pb.setVisibility(INVISIBLE);
                iv.setVisibility(VISIBLE);
                txtPull.setText("松开刷新");
                break;
            case STATE_REFRESHING:
                pb.setVisibility(VISIBLE);
                iv.setVisibility(INVISIBLE);
                txtPull.setText("正在加载");

        }
    }
}
