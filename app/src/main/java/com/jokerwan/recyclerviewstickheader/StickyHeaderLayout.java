package com.jokerwan.recyclerviewstickheader;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by JokerWan on 2019/2/21.
 * Function: RecyclerView中某个item悬浮置顶
 * 使用：
 *      <com.jokerwan.recyclerviewstickheader.StickyHeaderLayout
 *      android:id="@+id/sh_layout"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent">
 *
 *          <android.support.v7.widget.RecyclerView
 *          android:id="@+id/recycler"
 *          android:layout_width="match_parent"
 *          android:layout_height="match_parent" />
 *
 *      </com.jokerwan.recyclerviewstickheader.StickyHeaderLayout>
 */

public class StickyHeaderLayout extends FrameLayout {

    private Context mContext;
    private RecyclerView mRecyclerView;

    //吸顶容器，用于承载吸顶布局。
    private FrameLayout mStickyLayout;

    public static final int TYPE_STICKY_LAYOUT = 1001;

    private boolean showStickLayout;
    private int showStickItemPosition;

    public StickyHeaderLayout(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0 || !(child instanceof RecyclerView)) {
            //外界只能向StickyHeaderLayout添加一个RecyclerView,而且只能添加RecyclerView。
            throw new IllegalArgumentException("StickyHeaderLayout can host only one direct child，it must be RecyclerView");
        }
        super.addView(child, index, params);
        mRecyclerView = (RecyclerView) child;
        addOnScrollListener();
        addStickyLayout();
    }

    /**
     * 添加滚动监听
     */
    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // 在滚动的时候，需要不断的更新吸顶布局。
                Log.d("TAG", "onScrolled: dy-->" + dy);
                updateStickyView(dy);
            }
        });
    }

    /**
     * 添加吸顶容器
     */
    private void addStickyLayout() {
        mStickyLayout = new FrameLayout(mContext);
        FrameLayout.LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mStickyLayout.setLayoutParams(lp);
        mStickyLayout.setVisibility(GONE);
        super.addView(mStickyLayout, 1, lp);
    }

    /**
     * 更新吸顶布局。
     */
    private void updateStickyView(int dy) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        //获取列表显示的第一个项。
        int firstVisibleItemPosition;
        if (dy < 0) {
            // 向下滑动时吸顶布局在列表中的位置的上一个位置滑出屏幕时才隐藏头部吸顶布局，否则会导致头部吸顶布局还没有隐藏，列表吸顶布局就滑出
            firstVisibleItemPosition = getFirstVisibleItemPosition() + 1;
        } else {
            firstVisibleItemPosition = getFirstVisibleItemPosition();
        }
        if (adapter != null) {
            if (mStickyLayout.getChildCount() == 0) {
                RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(mStickyLayout, TYPE_STICKY_LAYOUT);
                mStickyLayout.addView(holder.itemView);
            }
            showStickLayout(firstVisibleItemPosition, dy);
        }

        //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
        if (mStickyLayout.getChildCount() > 0 && mStickyLayout.getHeight() == 0) {
            mStickyLayout.requestLayout();
        }
    }

    /**
     * 设置显示和隐藏吸顶布局
     *
     * @param firstVisibleItemPosition 第一个可见item的位置
     * @param dy                       recyclerView一次滑动的偏移量
     */
    public void showStickLayout(int firstVisibleItemPosition, int dy) {
        if (firstVisibleItemPosition > 0) {
            if (showStickLayout && dy < 0 && firstVisibleItemPosition <= showStickItemPosition) {
                showStickLayout = false;
                mStickyLayout.setVisibility(GONE);
                Log.d("TAG", "updateStickyView: --------------------gone");
            } else if (!showStickLayout && dy >= 0 && firstVisibleItemPosition >= showStickItemPosition) {
                showStickLayout = true;
                mStickyLayout.setVisibility(VISIBLE);
                Log.d("TAG", "updateStickyView: --------------------visible");
            }
        } else if (showStickItemPosition == 0) {
            showStickLayout = true;
            mStickyLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * 获取当前屏幕第一个显示的item .
     */
    private int getFirstVisibleItemPosition() {
        int firstVisibleItem = -1;
        RecyclerView.LayoutManager layout = mRecyclerView.getLayoutManager();
        if (layout != null) {
            if (layout instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = getMin(firstPositions);
            }
        }
        return firstVisibleItem;
    }

    private int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min)
                min = arr[x];
        }
        return min;
    }

    public void setShowStickItemPosition(int showStickItemPosition) {
        this.showStickItemPosition = showStickItemPosition;
    }
}

