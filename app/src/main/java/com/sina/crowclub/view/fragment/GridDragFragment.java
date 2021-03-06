package com.sina.crowclub.view.fragment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.crowclub.R;
import com.sina.crowclub.network.Parse.StoryBean;
import com.sina.crowclub.view.adapter.RecyleAdapter;
import com.sina.crowclub.view.base.BaseFragment;
import com.sina.crowclub.view.widget.helper.ItemTouchHelperAdapter;
import com.sina.crowclub.view.widget.helper.OnStartDragListener;
import com.sina.crowclub.view.widget.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wu on 2016/7/23.
 */
public class GridDragFragment extends BaseFragment {
    private static final String TAG = GridDragFragment.class.getSimpleName();

    /** View */
    private RecyclerView recyclerView;

    /** Data */
    private List<StoryBean> mData;
    private RecyleAdapter recyleAdapter;

    private OnStartDragListener mOnStartDragListener;
    private ItemTouchHelper mItemTouchHelper;

    /**********************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_series,null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        recyclerView = $(view,R.id.recyclerview);
        initData();
    }

    private void initData(){
        String tempName = "thisisianameandhasernothinngmeafulofthisworldbutjusttotestandtesttest";

        mData = new ArrayList<>();
        for(int i=0;i<100;i++){
            StoryBean bean = new StoryBean();
            bean.id = i;
            bean.name = tempName.substring(new Random().nextInt(5) + 1
                    ,new Random().nextInt(50) + 6 );

            if(TextUtils.isEmpty(bean.name))
                bean.albumName = "StoryName";

            bean.create_time = new Random().nextInt(1000);
            if(i % 3 == 0) {
                bean.albumId = new Random().nextInt(5);
                bean.albumName = tempName.substring(new Random().nextInt(5) + 1
                        ,new Random().nextInt(50) + 6);
                if(TextUtils.isEmpty(bean.albumName))
                    bean.albumName = "this is my Album";
            }
            mData.add(bean);
        }

        recyleAdapter = new RecyleAdapter(mData,getOnStartDragListener()
                ,null);

        recyclerView.setHasFixedSize(true); //改变recycleView 时，固定recycleView的数量大小
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);// 设置显示的方式，默认是垂直方向
        //RecyclerView.ItemDecoration itemDecoration = getItemDecoration(LinearLayoutManager.VERTICAL,1,0xFFDDDDDD);
        RecyclerView.ItemDecoration itemDecoration = getGridItemDecoration(getActivity().getResources().getDrawable(R.drawable.drawable_divider));
        recyclerView.addItemDecoration(itemDecoration); //设置分割线
        recyclerView.setAdapter(recyleAdapter);//设置adapter

        mItemTouchHelper = new ItemTouchHelper(getCallBack(recyleAdapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private OnStartDragListener getOnStartDragListener(){
        mOnStartDragListener = new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        };
        return mOnStartDragListener;
    }

    private ItemTouchHelper.Callback getCallBack(ItemTouchHelperAdapter adapter){
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        return callback;
    }

    //自定义的itemDecoration
    private RecyclerView.ItemDecoration getItemDecoration(final int _orientation,final int _size,final int _color){
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            /**
             * 水平方向的
             */
            public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

            /**
             * 垂直方向的
             */
            public static final int VERTICAL = LinearLayoutManager.VERTICAL;

            // 画笔
            private Paint paint = new Paint();

            // 布局方向
            private int orientation = _orientation;
            // 分割线颜色
            private int color = _color;
            // 分割线尺寸
            private int size = _size;

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);

                if (orientation == VERTICAL) {
                    drawHorizontal(c, parent);
                } else {
                    drawVertical(c, parent);
                }
            }

            /**
             * 设置分割线颜色
             *
             * @param color 颜色
             */
            public void setColor(int color) {
                this.color = color;
                paint.setColor(color);
            }

            /**
             * 设置分割线尺寸
             *
             * @param size 尺寸
             */
            public void setSize(int size) {
                this.size = size;
            }

            // 绘制垂直方向的分割线
            protected void drawVertical(Canvas c, RecyclerView parent) {
                final int top = parent.getPaddingTop();
                final int bottom = parent.getHeight() - parent.getPaddingBottom();

                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int left = child.getRight() + params.rightMargin;
                    final int right = left + size;

                    c.drawRect(left, top, right, bottom, paint);
                }
            }

            // 绘制水平方向的分割线
            protected void drawHorizontal(Canvas c, RecyclerView parent) {
                final int left = parent.getPaddingLeft();
                final int right = parent.getWidth() - parent.getPaddingRight();

                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin;
                    final int bottom = top + size;

                    c.drawRect(left, top, right, bottom, paint);
                }
            }
        };
        return itemDecoration;
    }

    private RecyclerView.ItemDecoration getGridItemDecoration(final Drawable drawable){
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration(){
            private Drawable mDivider = drawable;

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){
                drawHorizontal(c, parent);
                drawVertical(c, parent);
            }

            private int getSpanCount(RecyclerView parent) {
                // 列数
                int spanCount = -1;
                LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    spanCount = ((StaggeredGridLayoutManager) layoutManager)
                            .getSpanCount();
                }
                return spanCount;
            }

            public void drawHorizontal(Canvas c, RecyclerView parent) {
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int left = child.getLeft() - params.leftMargin;
                    final int right = child.getRight() + params.rightMargin
                            + mDivider.getIntrinsicWidth();
                    final int top = child.getBottom() + params.bottomMargin;
                    final int bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }

            public void drawVertical(Canvas c, RecyclerView parent) {
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);

                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int top = child.getTop() - params.topMargin;
                    final int bottom = child.getBottom() + params.bottomMargin;
                    final int left = child.getRight() + params.rightMargin;
                    final int right = left + mDivider.getIntrinsicWidth();

                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }

            private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                        int childCount) {
                LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    {
                        return true;
                    }
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int orientation = ((StaggeredGridLayoutManager) layoutManager)
                            .getOrientation();
                    if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                        if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                        {
                            return true;
                        }
                    } else {
                        childCount = childCount - childCount % spanCount;
                        if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                            return true;
                    }
                }
                return false;
            }

            private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                      int childCount) {
                LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    childCount = childCount - childCount % spanCount;
                    if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                        return true;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int orientation = ((StaggeredGridLayoutManager) layoutManager)
                            .getOrientation();
                    // StaggeredGridLayoutManager 且纵向滚动
                    if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                        childCount = childCount - childCount % spanCount;
                        // 如果是最后一行，则不需要绘制底部
                        if (pos >= childCount)
                            return true;
                    } else{
                    // StaggeredGridLayoutManager 且横向滚动
                        // 如果是最后一行，则不需要绘制底部
                        if ((pos + 1) % spanCount == 0) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void getItemOffsets(Rect outRect, int itemPosition,
                                       RecyclerView parent) {
                int spanCount = getSpanCount(parent);
                int childCount = parent.getAdapter().getItemCount();
                if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
                {
                    outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
                {
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, mDivider.getIntrinsicWidth(),
                            mDivider.getIntrinsicHeight());
                }
            }
        };

        return itemDecoration;
    }
}
