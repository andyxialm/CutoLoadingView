package cn.refactor.cutoloadingviewdemo.header;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/10 18:12
 * Description :
 */
public class SamplePtrFrameLayout extends PtrFrameLayout {
    public SamplePtrFrameLayout(Context context) {
        this(context, null);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        CutoLoadingViewHeader mHeaderView = new CutoLoadingViewHeader(getContext());
        setResistance(1.2f);
        setLoadingMinTime(1000);
        setDurationToCloseHeader(1400);
        setDurationToClose(1000);
        setRatioOfHeaderHeightToRefresh(1.0f);
        setKeepHeaderWhenRefresh(true);
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }
}
