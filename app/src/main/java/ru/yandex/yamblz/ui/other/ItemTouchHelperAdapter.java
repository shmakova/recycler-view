package ru.yandex.yamblz.ui.other;

import android.support.v7.widget.RecyclerView;

/**
 * Created by shmakova on 26.07.16.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target);

    void onItemDismiss(RecyclerView.ViewHolder viewHolder);

    void onItemChange(RecyclerView.ViewHolder viewHolder);
}
