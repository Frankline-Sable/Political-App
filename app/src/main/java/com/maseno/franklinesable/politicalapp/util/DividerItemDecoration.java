package com.maseno.franklinesable.politicalapp.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Frankline Sable on 25/02/2017. from 07:37
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerItemDecoration(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int dividerLeft=parent.getPaddingLeft();
        int dividerRight=parent.getWidth()-parent.getPaddingRight();

        int childCount=parent.getChildCount();
        for(int i=0;i<childCount-1;i++){
            View child=parent.getChildAt(i);

            RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();

            int dividerTop=child.getBottom()+params.bottomMargin;
            int dividerBottom=dividerTop+mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft,dividerTop,dividerRight,dividerBottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)==0){
            return;
        }
        outRect.top=mDivider.getIntrinsicHeight();
    }
}
