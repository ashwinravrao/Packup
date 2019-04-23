package com.ashwinrao.boxray.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentListBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListFragment extends Fragment {

    private static final String TAG = "FAB";
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private LiveData<List<Box>> boxesLD;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        boxesLD = ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().getBoxes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        BottomAppBar appBar = binding.bottomAppBar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(appBar);

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(R.string.toolbar_title_home);
        toolbar.inflateMenu(R.menu.menu_toolbar_list);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.toolbar_search:
                        ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.title_search, true,true);
                        return true;
                }
                return true;
            }
        });

        ExtendedFloatingActionButton fab = binding.fabExtended;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.stay_still, R.anim.stay_still, R.anim.slide_out_to_right)
                        .replace(R.id.fragment_container, new AddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()), getActivity().getWindow().getDecorView().findViewById(R.id.fragment_container), boxesLD.getValue());
        recyclerView.setAdapter(listAdapter);

        boxesLD.observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                if(boxes != null) { listAdapter.setBoxes(boxes); }
                else { listAdapter.setBoxes(new ArrayList<Box>()); }
                recyclerView.setAdapter(listAdapter);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bottom_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.bottom_bar_about:
                ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.title_about, true, true);
                return true;
            case R.id.bottom_bar_settings:
                ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.title_settings, true, true);
                return true;
        }
        return true;
    }
}
