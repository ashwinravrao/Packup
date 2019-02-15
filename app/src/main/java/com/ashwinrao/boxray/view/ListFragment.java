package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentBoxListBinding;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private LiveData<List<Box>> mBoxesLD;
    private FloatingActionButton mFab;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.box_list_fragment_action_bar_title));
        ((MainActivity) getActivity()).setNavigationViewBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        mFragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(( Objects.requireNonNull(getActivity())).getApplication());
        final BoxViewModel viewModel = factory.create(BoxViewModel.class);
        mBoxesLD = viewModel.getBoxes();

        mFab = ((MainActivity) getActivity()).getFloatingActionButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFab.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentBoxListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_box_list, container, false);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new AddEditFragment(), "AddEditFragment")
                        .commit();
            }
        });

        mRecyclerView = binding.recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListAdapter(Objects.requireNonNull(getActivity()), mBoxesLD.getValue());
        mRecyclerView.setAdapter(mAdapter);

        mBoxesLD.observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                if(boxes != null) { mAdapter.setBoxes(boxes); }
                else { mAdapter.setBoxes(new ArrayList<Box>()); }
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return binding.getRoot();
    }
}
