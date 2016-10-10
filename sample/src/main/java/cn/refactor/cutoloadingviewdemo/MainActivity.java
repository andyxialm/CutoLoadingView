package cn.refactor.cutoloadingviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.cutoloadingviewdemo.header.SamplePtrFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity {

    private SamplePtrFrameLayout mPtrFrameLayout;
    private RecyclerView mRecyclerView;
    private SampleAdapter mAdapter;

    private List<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        mPtrFrameLayout = (SamplePtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(mPtrFrameLayout, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        mDataList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mAdapter = new SampleAdapter();
        mRecyclerView.setAdapter(mAdapter);
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

                for (int i = 0; i < 15; i ++) {
                    mDataList.add(String.valueOf(i));
                }
                mAdapter.notifyItemRangeChanged(0, mDataList.size() - 1);
                mPtrFrameLayout.refreshComplete();
            }
        }, 3000);
    }

    public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

        @Override
        public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new SampleAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SampleAdapter.ViewHolder holder, int position) {
            holder.bind(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextTv;

            ViewHolder(View itemView) {
                super(itemView);
                mTextTv = (TextView) itemView.findViewById(R.id.tv_text);
            }

            void bind(String text) {
                mTextTv.setText(text);
            }
        }
    }
}
