package com.ashwinrao.boxray.view.pages;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.PageBoxPropertiesBinding;
import com.ashwinrao.boxray.view.CommonActivity;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class BoxPropertiesPage extends Fragment {

    private BoxViewModel mViewModel;
    private boolean[] fieldHasInput = {false, false, false};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ((CommonActivity) Objects.requireNonNull(getActivity())).getViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final PageBoxPropertiesBinding binding = DataBindingUtil.inflate(inflater, R.layout.page_box_properties, container, false);

        final EditText[] fields = {binding.nameFieldContainer.getEditText(),
                binding.fromFieldContainer.getEditText(),
                binding.toFieldContainer.getEditText()};

        for (int i = 0; i < fields.length; i++) {
            watchField(Objects.requireNonNull(fields[i]), i, i+1);
        }

        return binding.getRoot();
    }

    private void watchField(@NonNull EditText editText, final int fieldIndex, final int boxArgIndex) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fieldHasInput[fieldIndex] = s.toString().length() > 0;
                mViewModel.setFieldsSatisfied(fieldHasInput[0] && fieldHasInput[1] && fieldHasInput[2]);
                mViewModel.boxArgs[boxArgIndex] = s.toString();
            }
        });
    }

}
