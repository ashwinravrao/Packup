package com.ashwinrao.locrate.view.fragment;

import android.app.Activity;
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
import android.widget.TextView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentAddBinding;
import com.ashwinrao.locrate.util.BackNavCallback;
import com.ashwinrao.locrate.util.CameraInitCallback;
import com.ashwinrao.locrate.util.ConfirmationDialog;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.CameraActivity;
import com.ashwinrao.locrate.view.adapter.ThumbnailAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.PhotoViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener, CameraInitCallback, BackNavCallback {

    private BoxViewModel viewModel;
    private PhotoViewModel photoViewModel;
    private FragmentAddBinding binding;
    private RecyclerView recyclerView;
    private ThumbnailAdapter adapter;
    private List<String> compoundedItems = new ArrayList<>();

    private final String PREF_ID_KEY = "next_available_id";
    private static final String TAG = "AddFragment";

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Locrate) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AddActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        photoViewModel = ViewModelProviders.of(getActivity(), factory).get(PhotoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);

        // data binding
        binding.setBoxNum(getBoxNumber());
        binding.setNumberOfItems(getString(R.string.num_items_default));

        // widgets
        setupToolbar(binding.toolbar);
        setupNameField(binding.nameEditText);
        setupDescriptionField(binding.descriptionEditText);
        setupRecyclerView(binding.recyclerView);
        setupPrioritySwitch(binding.prioritySwitch);
        setupFillBoxFab(binding.fillBoxFab);
        setupNestedScrollFabInteraction(binding.nestedScrollView, binding.fillBoxFab);

        return binding.getRoot();
    }

    private void setupNestedScrollFabInteraction(NestedScrollView nestedScrollView, ExtendedFloatingActionButton fillBoxFab) {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && fillBoxFab.getVisibility() == View.VISIBLE) {
                fillBoxFab.hide();
            }
            if (scrollY < oldScrollY && fillBoxFab.getVisibility() != View.VISIBLE) {
                fillBoxFab.show();
            }
        });
    }

    private void setupFillBoxFab(ExtendedFloatingActionButton fab) {
        fab.setOnClickListener(view -> startCamera());
    }

    private void setupPrioritySwitch(SwitchCompat unpackSwitch) {
        unpackSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.getBox().setPriority(isChecked));
    }

    private SharedPreferences getSharedPreferences(@NonNull Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    private int getBoxNumber() {
        // Retrieve next available id
        return getSharedPreferences(Objects.requireNonNull(getActivity())).getInt(PREF_ID_KEY, 1);
    }

    private void saveBoxNumber() {
        SharedPreferences sharedPref = getSharedPreferences(Objects.requireNonNull(getActivity()));
        int nextAvailableId = sharedPref.getInt(PREF_ID_KEY, 1);

        // Store next available id
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_ID_KEY, nextAvailableId + 1);
        editor.apply();  // .apply() >= .commit()
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), dpToPx(Objects.requireNonNull(getContext()), 150f), dpToPx(getContext(), 150f));
        adapter.registerStartCameraListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupDescriptionField(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String description = s.toString().length() > 0 ? s.toString() : null;
                viewModel.getBox().setDescription(description);
            }
        });
    }

    private void setupNameField(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String name = s.toString().length() > 0 ? s.toString() : null;
                viewModel.getBox().setName(name);
            }
        });
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.toolbar_add);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            closeWithConfirmation();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_done) {
            if (viewModel.getBox().getContents() == null || viewModel.getBox().getContents().size() < 1) {
                showEmptyBoxDialog();
                return true;
            } else {
                if (viewModel.saveBox()) {
                    saveBoxNumber();
                    Objects.requireNonNull(getActivity()).finish();
                    return true;
                } else {
                    binding.nameEditText.setError(getResources().getString(R.string.name_field_error_message));
                    binding.nameEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    public void onResume() {
        super.onResume();
        updateItems();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final ArrayList<String> paths = Objects.requireNonNull(data).getStringArrayListExtra("paths");
                if (paths != null) {
                    photoViewModel.setPaths(paths);
                }

                Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(0, 0, 0, R.anim.slide_down_out)
                        .replace(R.id.fragment_container, new PhotoReviewFragment())
                        .commit();
            }
        }
    }

    private void updateItems() {
        if (photoViewModel.getPaths() != null && !photoViewModel.getPaths().equals(compoundedItems)) {
            compoundedItems.addAll(photoViewModel.getPaths());
        }
        if (compoundedItems.size() > 0) {
            adapter.setPaths(compoundedItems);
            recyclerView.setAdapter(adapter);
            viewModel.getBox().setContents(compoundedItems);
            binding.setNumberOfItems(viewModel.getBox().getNumItems());
//            togglePhotoDependentViewVisibilities(new TextView[]{binding.contentsHeading, binding.numItems});
        }
    }

    private void togglePhotoDependentViewVisibilities(TextView[] textViews) {
        for (TextView textView : textViews) {
            if (textView.getVisibility() == View.GONE || textView.getVisibility() == View.INVISIBLE) {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void startCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        photoViewModel.clearPaths();
        startActivityForResult(intent, 1);
    }

    private void closeWithConfirmation() {
        if (viewModel.areChangesUnsaved()) {
            showUnsavedChangesDialog();
        } else {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private void showUnsavedChangesDialog() {
        ConfirmationDialog.make(getContext(), new String[]{
                getString(R.string.dialog_discard_box_title),
                getString(R.string.dialog_discard_box_message),
                getString(R.string.discard),
                getString(R.string.no)}, false, new int[]{ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent),
                ContextCompat.getColor(Objects.requireNonNull(getContext()), android.R.color.holo_red_dark)}, dialogInterface -> {
            if (photoViewModel.getPaths() != null) {
                for (String path : photoViewModel.getPaths()) {
                    new File(path).delete();
                }
            }
            Objects.requireNonNull(getActivity()).finish();
            return null;
        }, dialogInterface -> {
            dialogInterface.cancel();
            return null;
        });
    }

    private void showEmptyBoxDialog() {
        ConfirmationDialog.make(getContext(), new String[]{
                        getString(R.string.dialog_empty_box_title),
                        getString(R.string.dialog_empty_box_message),
                        getString(R.string.ok),
                        getString(R.string.discard)},
                false,
                new int[]{ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent),
                        ContextCompat.getColor(getContext(), R.color.colorAccent)},
                dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                }, dialogInterface -> {
                    Objects.requireNonNull(getActivity()).finish();
                    return null;
                });
    }

    @Override
    public void onBackPressed() {
        closeWithConfirmation();
    }
}
