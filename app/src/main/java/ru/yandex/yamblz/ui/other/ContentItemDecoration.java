package ru.yandex.yamblz.ui.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shmakova on 26.07.16.
 */

public class ContentItemDecoration extends RecyclerView.ItemDecoration {
    private static final int OFFSET = 16;
    private Paint paintBorder;

    public ContentItemDecoration() {
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(Color.DKGRAY);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(OFFSET);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position % 2 == 0) {
                final int left = child.getLeft() + OFFSET / 2;
                final int top = child.getTop() + OFFSET / 2;
                final int right = child.getRight() - OFFSET / 2;
                final int bottom = child.getBottom() - OFFSET / 2;
                c.drawRect(left, top, right, bottom, paintBorder);
            }
        }
    }
}