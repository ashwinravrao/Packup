package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ThumbnailAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private boolean nameError;
    private BoxViewModel viewModel;
    private FragmentAddBinding binding;
    private RecyclerView recyclerView;
    private ThumbnailAdapter adapter;

    private static final String TAG = "Boxray";

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
        recyclerView = binding.recyclerView;
        setupToolbar(binding.toolbar);
        setupNameField(binding.nameEditText);
        setupDescriptionField(binding.descriptionEditText);
        setupSwitches(new SwitchCompat[]{binding.prioritySwitch, binding.reviewAfterSwitch});
        setupRecyclerView(binding.recyclerView);
        binding.boxNumber.setText("# 29");
        return binding.getRoot();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(recyclerView);
        adapter = new ThumbnailAdapter(getContext(), (int) Utilities.dpToPx(Objects.requireNonNull(getContext()), 200f), (int) Utilities.dpToPx(getContext(), 200f));
        recyclerView.setAdapter(adapter);
    }

    private void addItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int spanCount = 2;
                int spacing = (int) Utilities.dpToPx(Objects.requireNonNull(getActivity()), 16f);

                if (position >= 0) {
                    int column = position % spanCount;

                    outRect.left = spacing - column * spacing / spanCount;
                    outRect.right = (column + 1) * spacing / spanCount;

                    if (position < spanCount) {
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing;
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
    }

    private void setupSwitches(SwitchCompat[] switches) {
        for(SwitchCompat s : switches) {
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(s.getId() == R.id.priority_switch) {
                    viewModel.getBox().setFavorite(isChecked);
                } else {
                    buttonView.setChecked(false);
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivityForResult(intent, 1);
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
                    .setPositiveButton("Yes", (dialog1, which) -> Objects.requireNonNull(getActivity()).finish())
                    .setNegativeButton("No", (dialog12, which) -> dialog12.cancel())
                    .create();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                List<String> paths = data.getStringArrayListExtra("paths");
                if(paths != null) {
                    adapter.setPaths(paths);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }
}
