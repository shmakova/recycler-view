package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.other.ContentItemTouchHelperCallback;
import timber.log.Timber;

public class ContentFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.spinner)
    Spinner spinner;

    private GridLayoutManager gridLayoutManager;
    private DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, items);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(spinnerAdapter);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.supportsPredictiveItemAnimations();

        rv.setLayoutManager(gridLayoutManager);
        ContentAdapter contentAdapter = new ContentAdapter();
        rv.setAdapter(contentAdapter);
        rv.setItemAnimator(defaultItemAnimator);

        ItemTouchHelper.Callback callback =
                new ContentItemTouchHelperCallback(contentAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spanCount = Integer.valueOf(parent.getItemAtPosition(position).toString());
        Timber.d(String.valueOf(spanCount));
        gridLayoutManager.setSpanCount(spanCount);
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
