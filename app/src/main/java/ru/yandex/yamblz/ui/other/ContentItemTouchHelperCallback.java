package ru.yandex.yamblz.ui.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


/**
 * Created by shmakova on 26.07.16.
 */

public class ContentItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter adapter;
    private final PairItemDecoration pairItemDecoration;
    private Paint redPaint;

    public ContentItemTouchHelperCallback(ItemTouchHelperAdapter adapter,
                                          PairItemDecoration pairItemDecoration) {
        this.adapter = adapter;
        this.pairItemDecoration = pairItemDecoration;
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.onItemMove(recyclerView, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        pairItemDecoration.setFirstItemPosition(viewHolder.getAdapterPosition());
        pairItemDecoration.setLastItemPosition(target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                         int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float width = (float) itemView.getWidth();
            float alpha = Math.abs(dX) / width;
            itemView.setTranslationX(dX);
            redPaint.setAlpha((int) (alpha * 255));
            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                    (float) itemView.getBottom(), redPaint);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }
}
