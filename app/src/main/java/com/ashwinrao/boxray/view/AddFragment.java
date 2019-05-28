package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private boolean nameError;
    private BoxViewModel viewModel;
    private FragmentAddBinding binding;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);
        setupToolbar(binding.toolbar);
        setupNameField(binding.nameEditText);
        setupDescriptionField(binding.descriptionEditText);
        setupSwitches(new SwitchCompat[]{binding.prioritySwitch, binding.reviewAfterSwitch});
        setupRecyclerView(binding.recyclerView);
        binding.boxNumber.setText("# 29");
        return binding.getRoot();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));

    }

    private void setupSwitches(SwitchCompat[] switches) {
        for(SwitchCompat s : switches) {
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(s.getId() == R.id.priority_switch) {
                    viewModel.getBox().setFavorite(isChecked);
                } else {
                    buttonView.setChecked(false);
                    Intent intent = new Intent(getContext(), CameraActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void setupDescriptionField(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    viewModel.getBox().setDescription(s.toString());
                } else {
                    viewModel.getBox().setDescription(null);
                }
            }
        });
    }

    private void setupNameField(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    viewModel.getBox().setName(s.toString());
                } else {
                    viewModel.getBox().setName(null);
                }
            }
        });
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_add);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Discard current box?")
                    .setMessage("You may have unsaved changes. Are you sure you want to leave and discard this box?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
//            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.top_row_view_holder, Objects.requireNonNull(getActivity()).getTheme()));
//            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light, getActivity().getTheme()));
            dialog.show();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_done) {
            if(viewModel.saveBox()) {
                Objects.requireNonNull(getActivity()).finish();
                return true;
            } else {
                nameError = true;
                binding.nameEditText.setError("Give your box a name");
                binding.nameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        nameError = false;
                        binding.nameEditText.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                return true;
            }
        }
        return false;
    }
}
