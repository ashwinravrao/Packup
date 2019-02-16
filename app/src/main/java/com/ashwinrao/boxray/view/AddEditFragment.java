package com.ashwinrao.boxray.view;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddEditBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class AddEditFragment extends Fragment {

    private int mOriginalSoftInputMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        if(fab != null) { fab.setVisibility(View.GONE); }

        mOriginalSoftInputMode = Utilities.applyFragmentSoftInput(getActivity(), null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false);
        configureInputFields(binding);
        configureAddItemField(binding);

        binding.choosePhotoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add image picker with option for launching camera
                Snackbar.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().findViewById(R.id.drawer_layout), "Add more items", Snackbar.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void configureAddItemField(@NonNull final FragmentAddEditBinding binding) {
        binding.addItemFieldEditable.setHint(R.string.add_item_field_title);
        binding.addItemFieldEditable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    binding.addItemImageView.setImageResource(R.drawable.ic_add_item_enabled);
                } else {
                    binding.addItemImageView.setImageResource(R.drawable.ic_add_item_disabled);
                }
            }
        });
    }

    private void configureInputFields(@NonNull final FragmentAddEditBinding binding) {
        String[] field_titles = {getString(R.string.name_field_title), getString(R.string.source_field_title), getString(R.string.dest_field_title)};
        final View[] views = {binding.nameInput, binding.sourceInput, binding.destInput};

        for (int i = 0; i < views.length; i++) {
            final TextView hint = views[i].findViewById(R.id.input_field_hint);
            final EditText editable = views[i].findViewById(R.id.input_field_editable);

            hint.setText(field_titles[i]);
            editable.setHint(field_titles[i]);

            editable.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().length() > 0) {
                        hint.setVisibility(View.VISIBLE);
                    } else {
                        hint.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utilities.applyFragmentSoftInput(getActivity(), mOriginalSoftInputMode);
    }
}
