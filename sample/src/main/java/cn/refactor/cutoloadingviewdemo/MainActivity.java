package cn.refactor.cutoloadingviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.cutoloadingview.CutoLoadingView;
import cn.refactor.cutoloadingviewdemo.header.SamplePtrFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/11 14:33
 * Description : Sample activity (sample images from UnSplash)
 */
public class MainActivity extends AppCompatActivity {

    private SamplePtrFrameLayout mPtrFrameLayout;
    private RecyclerView         mRecyclerView;
    private ListAdapter          mAdapter;

    private CutoLoadingView      mCutoLoadingView;
    private List<Integer>        mDataList;
    private boolean              mPullRefreshEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setupViews() {
        mCutoLoadingView = (CutoLoadingView) findViewById(R.id.cuto_loading_view);
        mPtrFrameLayout = (SamplePtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mPullRefreshEnable && PtrDefaultHandler.checkContentCanBePulledDown(mPtrFrameLayout, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (frame instanceof SamplePtrFrameLayout) {
                    ((SamplePtrFrameLayout) frame).onRefreshBegin();
                }
                refresh();
            }
        });
        mDataList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mAdapter = new ListAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL,
                20, getResources().getColor(android.R.color.transparent));
        mRecyclerView.addItemDecoration(divider);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.scroll_refresh:
                        mPullRefreshEnable = true;
                        mPtrFrameLayout.setPullToRefresh(true);
                        mCutoLoadingView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        if (mDataList != null && mDataList.size() > 0) {
                            mDataList.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        autoRefresh();
                        break;
                    case R.id.regular_refresh:
                        if (mPtrFrameLayout.isRefreshing()) {
                            mPtrFrameLayout.refreshComplete();
                        }
                        mPullRefreshEnable = false;
                        mPtrFrameLayout.setPullToRefresh(false);
                        mRecyclerView.setVisibility(View.GONE);

                        mCutoLoadingView.setVisibility(View.VISIBLE);
                        mCutoLoadingView.startLoadingAnim();

                        if (mDataList != null && mDataList.size() > 0) {
                            mDataList.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        regularRefresh();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        autoRefresh();
    }

    private void autoRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 500);
    }

    /**
     * pull to refresh
     */
    private void refresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDataList == null) {
                    mDataList = new ArrayList<>();
                } else {
                    mDataList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                for (int i = 0; i < 20; i ++) {
                    if (0 == i % 3) {
                        mDataList.add(R.mipmap.ic_image_0);
                    } else if (1 == i % 3) {
                        mDataList.add(R.mipmap.ic_image_1);
                    } else {
                        mDataList.add(R.mipmap.ic_image_2);
                    }
                }
                mAdapter.notifyItemRangeChanged(0, mDataList.size() - 1);
                mPtrFrameLayout.refreshComplete();
            }
        }, 3000);
    }

    /**
     * regular refresh
     */
    private void regularRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCutoLoadingView.setVisibility(View.GONE);
                mCutoLoadingView.stopLoadingAnim();
                mRecyclerView.setVisibility(View.VISIBLE);
                if (mDataList == null) {
                    mDataList = new ArrayList<>();
                } else {
                    mDataList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                for (int i = 0; i < 20; i ++) {
                    if (0 == i % 3) {
                        mDataList.add(R.mipmap.ic_image_0);
                    } else if (1 == i % 3) {
                        mDataList.add(R.mipmap.ic_image_1);
                    } else {
                        mDataList.add(R.mipmap.ic_image_2);
                    }
                }
                mAdapter.notifyItemRangeChanged(0, mDataList.size() - 1);
            }
        }, 3000);
    }

}
