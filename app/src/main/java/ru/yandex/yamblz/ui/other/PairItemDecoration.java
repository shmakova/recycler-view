package ru.yandex.yamblz.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.yandex.yamblz.R;

/**
 * Created by shmakova on 28.07.16.
 */

public class PairItemDecoration extends RecyclerView.ItemDecoration {
    private static final int OFFSET = 16;
    private Paint paintBorder;
    private Context context;
    private int firstItemPosition = -1;
    private int lastItemPosition = -1;

    public void setFirstItemPosition(int firstItemPosition) {
        this.firstItemPosition = firstItemPosition;
    }

    public void setLastItemPosition(int lastItemPosition) {
        this.lastItemPosition = lastItemPosition;
    }

    public PairItemDecoration(Context context) {
        this.context = context;
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(Color.YELLOW);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(OFFSET);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        final RecyclerView.ViewHolder firstItem = parent
                .findViewHolderForAdapterPosition(firstItemPosition);
        final RecyclerView.ViewHolder lastItem = parent
                .findViewHolderForAdapterPosition(lastItemPosition);

        if (firstItem != null) {
            drawCheckCircle(c, layoutManager, firstItem.itemView);
        }

        if (lastItem != null) {
            drawCheckCircle(c, layoutManager, lastItem.itemView);
        }

    }

    private void drawCheckCircle(Canvas c, RecyclerView.LayoutManager layoutManager, View view) {
        Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black_24dp);
        d.setBounds(layoutManager.getDecoratedRight(view) - d.getMinimumWidth() - OFFSET,
                layoutManager.getDecoratedTop(view) + OFFSET,
                layoutManager.getDecoratedRight(view) - OFFSET,
                layoutManager.getDecoratedTop(view) + d.getMinimumHeight() + OFFSET);
        d.draw(c);
    }
}