package cn.refactor.cutoloadingviewdemo.header;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import cn.refactor.cutoloadingview.CutoLoadingView;
import cn.refactor.cutoloadingviewdemo.R;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/10 17:55
 * Description :CutoLoadingViewHeader
 */
public class CutoLoadingViewHeader extends FrameLayout implements PtrUIHandler {

    private CutoLoadingView mCutoLoadingView;

    public CutoLoadingViewHeader(Context context) {
        this(context, null);
    }

    public CutoLoadingViewHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutoLoadingViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CutoLoadingViewHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.header_loading_layout, null);
        mCutoLoadingView = (CutoLoadingView) contentView.findViewById(R.id.loading_view);
        mCutoLoadingView.setMax(1.0f);
        addView(contentView);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mCutoLoadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mCutoLoadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mCutoLoadingView.startLoadingAnim();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mCutoLoadingView.stopLoadingAnim();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        mCutoLoadingView.setProgress(ptrIndicator.getCurrentPercent() > 1 ? 1 : ptrIndicator.getCurrentPercent());
    }
}
