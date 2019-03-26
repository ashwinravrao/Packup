package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentBoxListBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

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

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private LiveData<List<Box>> boxesLD;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.box_list_fragment_action_bar_title));
        ((MainActivity) getActivity()).setNavigationViewBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(( Objects.requireNonNull(getActivity())).getApplication());
        final BoxViewModel viewModel = factory.create(BoxViewModel.class);

        boxesLD = viewModel.getBoxes();
    }

    @Override
    public void onResume() {
        super.onResume();

        View root = Objects.requireNonNull(this.getView()).getRootView();
        if(Utilities.keyboardIsShowing(root)) {
            Utilities.hideKeyboardFrom(Objects.requireNonNull(getActivity()), root);
        }

        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.box_list_fragment_action_bar_title));
        ((MainActivity) Objects.requireNonNull(getActivity())).resetDrawerToggle();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentBoxListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_box_list, container, false);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CommonActivity.class);
//                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_from_right, R.anim.stay_still);
//                startActivity(intent, activityOptions.toBundle());

                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.stay_still, R.anim.stay_still, R.anim.slide_out_to_right)
                        .replace(R.id.fragment_container_main, new AddEditFragment(), "AddEditFragment")
                        .commit();
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()), boxesLD.getValue());
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
}
