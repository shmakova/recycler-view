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
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position % 2 == 0) {
                c.drawRect(
                        layoutManager.getDecoratedLeft(child) + OFFSET / 2,
                        layoutManager.getDecoratedTop(child) + OFFSET / 2,
                        layoutManager.getDecoratedRight(child) - OFFSET / 2,
                        layoutManager.getDecoratedBottom(child) - OFFSET / 2,
                        paintBorder);
            }
        }
    }
}