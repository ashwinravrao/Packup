package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentDetailBinding;
import com.ashwinrao.boxray.view.adapter.ItemAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DetailFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private BoxViewModel viewModel;
    private LiveData<Box> liveBox;
    private static final String TAG = "Boxray";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(Objects.requireNonNull(getActivity()).getApplication());
        viewModel = ViewModelProviders.of(getActivity(), factory).get(BoxViewModel.class);

        if(getArguments() != null) {
            liveBox = viewModel.getBoxByID(getArguments().getInt("ID"));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        Toolbar toolbar = binding.toolbar;
        toolbar.inflateMenu(R.menu.menu_toolbar_detail);
        toolbar.setOnMenuItemClickListener(this);

        toolbar.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ItemAdapter adapter = new ItemAdapter(getActivity(), ((MainActivity) Objects.requireNonNull(getActivity())).getFragmentContainerView(), null, null, false);

        liveBox.observe(this, new Observer<Box>() {
            @Override
            public void onChanged(Box box) {
                binding.setBox(box);
                binding.boxNumber.setText(getString(R.string.title_detail_box_number, box.getId()));
                if(box.getNotes() == null) binding.noteContainer.setVisibility(View.GONE);
                adapter.setItems(box.getContents());
                binding.recyclerView.setAdapter(adapter);
            }
        });

        return binding.getRoot();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit:
                Snackbar.make(getView(), "Editing mode", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return true;
    };
}
