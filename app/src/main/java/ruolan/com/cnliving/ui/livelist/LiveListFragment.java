package ruolan.com.cnliving.ui.livelist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ruolan.com.cnliving.R;

/**
 * Created by wuyinlei on 2017/11/14.
 */

public class LiveListFragment extends Fragment {

    private ListView mLiveListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_list, container, false);
        findAllViews(view);
        return view;
    }

    private void findAllViews(View view) {
        Toolbar titlebar = (Toolbar) view.findViewById(R.id.titlebar);
        titlebar.setTitle("热播列表");
        titlebar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(titlebar);

        mLiveListView = (ListView) view.findViewById(R.id.live_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求服务器，获取直播列表
                requestLiveList();
            }
        });
    }

    private void requestLiveList() {



    }
}
