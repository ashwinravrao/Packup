package com.ashwinrao.boxray.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.BackNavCallback;
import com.ashwinrao.boxray.util.CameraInitCallback;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ThumbnailAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.PhotoViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener, CameraInitCallback, BackNavCallback {

    private boolean[] fieldsUnsaved = new boolean[]{false, false, false};
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
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        photoViewModel = ViewModelProviders.of(getActivity(), factory).get(PhotoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);

        // data binding
        binding.setBoxNum(getBoxNumber());
        binding.setNumItems(getString(R.string.num_items_default));

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
            if(scrollY > oldScrollY && fillBoxFab.getVisibility() == View.VISIBLE) {
                fillBoxFab.hide();
            } if(scrollY < oldScrollY && fillBoxFab.getVisibility() != View.VISIBLE) {
                fillBoxFab.show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }

    private void setupFillBoxFab(ExtendedFloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            startCamera();
        });
    }

    private void setupPrioritySwitch(SwitchCompat unpackSwitch) {
        unpackSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.getBox().setPriority(isChecked);
        });
    }

    private SharedPreferences getSharedPreferences(@NonNull Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    // todo consider moving to db for access
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
        Utilities.addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), Utilities.dpToPx(Objects.requireNonNull(getContext()), 150f), Utilities.dpToPx(getContext(), 150f));
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
                if (s.toString().length() > 0) {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
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
            if (fieldsUnsaved[0] || fieldsUnsaved[1] || fieldsUnsaved[2]) {
                createUnsavedChangesDialog(getContext());
            } else {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void createUnsavedChangesDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_discard_box_title))
                .setMessage(getString(R.string.dialog_discard_box_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), (dialog1, which) -> {
                    if(photoViewModel.getPaths() != null) {
                        for (String path : photoViewModel.getPaths()) {
                            new File(path).delete();
                        }
                    }
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog12, which) -> dialog12.cancel())
                .create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    private void createEmptyBoxDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_empty_box_title))
                .setMessage(getResources().getString(R.string.dialog_empty_box_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), (dialog1, which) -> {
                    dialog1.cancel();
                })
                .setNegativeButton(getResources().getString(R.string.discard), (dialog12, which) -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack())
                .create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_done) {
            if (viewModel.getBox().getContents() == null || viewModel.getBox().getContents().size() < 1) {
                createEmptyBoxDialog(getContext());
                return true;
            } else {
                if (viewModel.saveBox()) {
                    saveBoxNumber();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                ArrayList<String> paths = data.getStringArrayListExtra("paths");
                if(paths != null) {
                    compoundedItems.addAll(paths);
                    fieldsUnsaved[2] = true;
                    adapter.setPaths(paths);
                    recyclerView.setAdapter(adapter);
                    photoViewModel.setPaths(paths);
                    viewModel.getBox().setContents(compoundedItems);
                    binding.setNumItems(viewModel.getBox().getNumItems());
                }

//                photoViewModel.setPaths(paths);
//                Objects.requireNonNull(getActivity())
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)
//                        .replace(R.id.fragment_container, new PhotoReviewFragment())
//                        .commit();
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
        if (fieldsUnsaved[0] || fieldsUnsaved[1] || fieldsUnsaved[2]) {
            createUnsavedChangesDialog(getContext());
        } else {
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}
