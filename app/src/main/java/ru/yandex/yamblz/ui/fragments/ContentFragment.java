package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.other.ContentItemAnimator;
import ru.yandex.yamblz.ui.other.ContentItemDecoration;
import ru.yandex.yamblz.ui.other.ContentItemTouchHelperCallback;
import ru.yandex.yamblz.ui.other.PairItemDecoration;

public class ContentFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {
    private static final int MAX_SPAN_COUNT = 50;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.decoration_checkbox)
    CheckBox decorationCheckbox;

    private GridLayoutManager gridLayoutManager;
    private ContentItemAnimator contentItemAnimator = new ContentItemAnimator();
    private ContentItemDecoration contentItemDecoration;
    private PairItemDecoration pairItemDecoration;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Integer[] items = range(1, MAX_SPAN_COUNT);
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, items);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(spinnerAdapter);

        contentItemDecoration = new ContentItemDecoration();
        pairItemDecoration = new PairItemDecoration(getContext());
        rv.addItemDecoration(pairItemDecoration);

        decorationCheckbox.setOnCheckedChangeListener(this);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);

        rv.setLayoutManager(gridLayoutManager);
        ContentAdapter contentAdapter = new ContentAdapter();
        contentAdapter.setHasStableIds(true);
        rv.setAdapter(contentAdapter);
        rv.setItemAnimator(contentItemAnimator);

        ItemTouchHelper.Callback callback =
                new ContentItemTouchHelperCallback(contentAdapter, pairItemDecoration);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(rv);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spanCount = Integer.valueOf(parent.getItemAtPosition(position).toString());
        gridLayoutManager.setSpanCount(spanCount);
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static Integer[] range(int min, int max) {
        int size = max - min + 1;
        Integer[] array = new Integer[size];

        for (int i = 0; i < size; i++) {
            array[i] = min + i;
        }

        return array;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            rv.addItemDecoration(contentItemDecoration);
        } else {
            rv.removeItemDecoration(contentItemDecoration);
        }
    }
}
