package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.BackNavigationListener;
import com.ashwinrao.boxray.util.StartCameraListener;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener, StartCameraListener, BackNavigationListener {

    private boolean nameError;
    private boolean[] fieldsUnsaved = new boolean[]{false, false, false};
    private BoxViewModel viewModel;
    private FragmentAddBinding binding;
    private RecyclerView recyclerView;
    private ThumbnailAdapter adapter;

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
        ((AddActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);
        binding.setBoxNum(getBoxNumber());
        binding.setNumItems("No items");
        recyclerView = binding.recyclerView;
        setupToolbar(binding.toolbar);
        setupNameField(binding.nameEditText);
        setupDescriptionField(binding.descriptionEditText);
        setupRecyclerView(binding.recyclerView);
        setupAddStuffButton(binding.addStuffButton);
//        setupFavoriteButton(binding.favoriteButton);
        setupUnpackSwitch(binding.prioritySwitch);
        return binding.getRoot();
    }

    private void setupUnpackSwitch(SwitchCompat unpackSwitch) {
        unpackSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.getBox().setPriority(isChecked);
        });
    }

    private void setupFavoriteButton(ImageButton button) {
        button.setOnClickListener(view -> {
            button.setSelected(!button.isSelected());
            viewModel.getBox().setFavorite(button.isSelected());
        });
    }

    private int getBoxNumber() {
        String key = "next_available_id";
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);

        // Retrieve next available id for this box
        return sharedPref.getInt(key, 1);
    }


    private void saveBoxNumber() {
        String key = "next_available_id";
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        int nextAvailableId = sharedPref.getInt(key, 1);

        // Store next available id for the next box
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, nextAvailableId + 1);
        editor.apply();
    }

    private void setupAddStuffButton(LinearLayout button) {
        button.setOnClickListener(view -> {
            startCamera();
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Utilities.addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), Utilities.dpToPx(Objects.requireNonNull(getContext()), 150f), Utilities.dpToPx(getContext(), 150f));
        adapter.registerStartCameraListener(this);
        recyclerView.setAdapter(adapter);
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
                    fieldsUnsaved[1] = true;
                } else {
                    viewModel.getBox().setDescription(null);
                    fieldsUnsaved[1] = false;
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
                    fieldsUnsaved[0] = true;
                } else {
                    viewModel.getBox().setName(null);
                    fieldsUnsaved[0] = false;
                }
            }
        });
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.toolbar_add);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog dialog = createUnsavedChangesDialog(getContext());
            if(fieldsUnsaved[0] || fieldsUnsaved[1] || fieldsUnsaved[2]) {
                dialog.show();
            } else {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private AlertDialog createUnsavedChangesDialog(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(getResources().getString(R.string.dialog_discard_box_title))
                .setMessage(getResources().getString(R.string.dialog_discard_box_message))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog1, which) -> Objects.requireNonNull(getActivity()).finish())
                .setNegativeButton("No", (dialog12, which) -> dialog12.cancel())
                .create();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_done) {
            if(viewModel.getBox().getContents() == null || viewModel.getBox().getContents().size() < 1) {
                Toast.makeText(getActivity(), "Box is empty, nothing to save", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getActivity()).finish();
            } else {
                if (viewModel.saveBox()) {
                    saveBoxNumber();
                    Objects.requireNonNull(getActivity()).finish();
                    return true;
                } else {
                    nameError = true;
                    binding.nameEditText.setError("Give your box a name");
                    binding.nameEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            nameError = false;
                            binding.nameEditText.setError(null);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    return true;
                }
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
                    if(binding.addStuffButton.getVisibility() == View.VISIBLE) { binding.addStuffButton.setVisibility(View.GONE); }
                    fieldsUnsaved[2] = true;
                    adapter.setPaths(paths);
                    recyclerView.setAdapter(adapter);
                    viewModel.getBox().setContents(paths);
                    binding.setNumItems(viewModel.getBox().getNumItems());
                }
            }
        }
    }

    @Override
    public void startCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = createUnsavedChangesDialog(getContext());
        if(fieldsUnsaved[0] || fieldsUnsaved[1] || fieldsUnsaved[2]) {
            dialog.show();
        } else {
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}
