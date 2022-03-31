package com.nepalese.virgosdk.VirgoView.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author nepalese on 2021/1/13 17:37
 * @usage 内部项全部显示，用于内嵌view
 * android中，不能在一个拥有Scrollbar的组件中嵌入另一个拥有Scrollbar的组件，因为这不科学，会混淆滑动事件，导致只显示一到两行数据。
 * 首先让子控件的内容全部显示出来，禁用了它的滚动;
 * 如果超过了父控件的范围则显示父控件的scrollbar滚动显示内容;
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
