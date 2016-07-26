package ru.yandex.yamblz.ui.adapters;

/**
 * Created by shmakova on 26.07.16.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onItemChange(int position);
}
