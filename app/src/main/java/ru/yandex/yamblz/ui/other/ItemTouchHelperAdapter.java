package ru.yandex.yamblz.ui.other;

import android.support.v7.widget.RecyclerView;

/**
 * Created by shmakova on 26.07.16.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(RecyclerView recyclerView, int fromPosition,
                    int toPosition);

    void onItemDismiss(int position);

    void onItemChange(int position);
}
