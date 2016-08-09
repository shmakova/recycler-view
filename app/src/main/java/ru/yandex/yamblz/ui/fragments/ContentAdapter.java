package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
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

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_item, parent, false);
        return new ContentHolder(container);
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getItemId(int position) {
        createColorForPosition(position);
        return colors.get(position);
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
    public void onItemMove(RecyclerView recyclerView, int fromPosition, int toPosition) {
        if (fromPosition != RecyclerView.NO_POSITION &&
                toPosition != RecyclerView.NO_POSITION) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(colors, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(colors, i, i - 1);
                }
            }

            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        if (position != RecyclerView.NO_POSITION) {
            colors.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void onItemChange(int position) {
        if (position != RecyclerView.NO_POSITION) {
            int color = generateColor();
            colors.set(position, color);
            notifyItemChanged(position);
        }
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> onItemChange(getAdapterPosition()));
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }
    }
}
