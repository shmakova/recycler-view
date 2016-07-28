package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.other.ItemTouchHelperAdapter;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements ItemTouchHelperAdapter {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();
    private int firstReplacedItemPosition = -1;
    private int lastReplacedItemPosition = -1;


    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        ContentHolder contentHolder = new ContentHolder(container);
        container.setOnClickListener(v -> onItemChange(contentHolder));
        return contentHolder;
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));

        if (position == firstReplacedItemPosition || position == lastReplacedItemPosition) {
            holder.addIcon();
        } else {
            holder.removeIcon();
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer createColorForPosition(int position) {
        if (position >= colors.size()) {
            colors.add(generateColor());
        }
        return colors.get(position);
    }

    private Integer generateColor() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }


    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                           RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        if (fromPosition != toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(colors, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(colors, i, i - 1);
                }
            }

            clearLastSelections(recyclerView);
            firstReplacedItemPosition = fromPosition;
            lastReplacedItemPosition = toPosition;

            ((ContentHolder) viewHolder).addIcon();
            ((ContentHolder) target).addIcon();

            notifyItemMoved(fromPosition, toPosition);
        }
    }

    private void clearLastSelections(RecyclerView recyclerView) {
        ContentHolder firstReplacedItem = (ContentHolder) recyclerView
                .findViewHolderForAdapterPosition(firstReplacedItemPosition);
        ContentHolder lastReplacedItem = (ContentHolder) recyclerView
                .findViewHolderForAdapterPosition(lastReplacedItemPosition);

        if (firstReplacedItem != null) {
            firstReplacedItem.removeIcon();
        }

        if (lastReplacedItem != null) {
            lastReplacedItem.removeIcon();
        }

        notifyItemRangeChanged(((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition(),
                ((GridLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() -
                        ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        colors.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemChange(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();

        if (position != RecyclerView.NO_POSITION) {
            int color = generateColor();
            colors.set(position, color);
            notifyItemChanged(position);
        }
    }

    public static class ContentHolder extends RecyclerView.ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }

        public void addIcon() {
            ((TextView) itemView).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_check_circle_black_24dp, 0);
        }

        public void removeIcon() {
            ((TextView) itemView).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, 0, 0);
        }
    }
}
