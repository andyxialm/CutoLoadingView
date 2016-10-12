package cn.refactor.cutoloadingviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.cutoloadingview.CutoLoadingView;
import cn.refactor.cutoloadingviewdemo.header.SamplePtrFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/12 09:24
 * Description : Pull to refresh
 */
public class RefreshFragment extends Fragment {

    public static final String ARG_TYPE          = "arg_type";
    public static final int TYPE_PULL_TO_REFRESH = 0;
    public static final int TYPE_REGULAR_REFRESH = 1;

    private SamplePtrFrameLayout mPtrFrameLayout;
    private RecyclerView         mRecyclerView;
    private ListAdapter          mAdapter;

    private CutoLoadingView      mCutoLoadingView;
    private List<Integer>        mDataList;
    private boolean              mPullRefreshEnable = true;

    public static RefreshFragment newInstance(int type) {
        RefreshFragment fragment = new RefreshFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup views
        mCutoLoadingView = (CutoLoadingView) view.findViewById(R.id.cuto_loading_view);
        mPtrFrameLayout = (SamplePtrFrameLayout) view.findViewById(R.id.ptr_frame_layout);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mAdapter = new ListAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
                20, getResources().getColor(android.R.color.transparent));
        mRecyclerView.addItemDecoration(divider);

        int type = getArguments().getInt(ARG_TYPE, TYPE_PULL_TO_REFRESH);
        if (TYPE_PULL_TO_REFRESH == type) {
            mPullRefreshEnable = true;
            mPtrFrameLayout.setPullToRefresh(true);
            mCutoLoadingView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            if (mDataList != null && mDataList.size() > 0) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
            }
            autoRefresh();
        } else if (TYPE_REGULAR_REFRESH == type) {
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
        }
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
