package com.ashwinrao.boxray.view.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentNumberPageFourBinding;
import com.ashwinrao.boxray.view.AddActivity;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class NumberPageFourFragment extends Fragment {

    private BoxViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(Objects.requireNonNull(getActivity()).getApplication());
        viewModel = factory.create(BoxViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentNumberPageFourBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_number_page_four, container, false);

        binding.toolbar.setTitle(getString(R.string.title_page_four_number));

        configureFinishButton(binding.finishButton);

        return binding.getRoot();
    }

    private void configureFinishButton(@NonNull Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.getCurrentBox().setId(generateRandomInteger());  // todo replace with sequential numbering scheme
                viewModel.verifySaveRequirements();
            }
        });
    }

    private int generateRandomInteger() {
        Random r = new Random();
        return r.nextInt(100);
    }

}
