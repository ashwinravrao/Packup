package com.ashwinrao.boxray.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.StartCameraListener;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ThumbnailAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener, StartCameraListener {

    private boolean nameError;
    private BoxViewModel viewModel;
    private FragmentAddBinding binding;
    private RecyclerView recyclerView;
    private ThumbnailAdapter adapter;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 5; // value is arbitrary

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
        setBoxNumber(binding.boxNumber);
        setupDescriptionField(binding.descriptionEditText);
        setupSwitches(new SwitchCompat[]{binding.prioritySwitch, binding.reviewAfterSwitch});
        setupRecyclerView(binding.recyclerView);
        return binding.getRoot();
    }

    private void setBoxNumber(TextView textView) {
        String key = "next_available_id";
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);

        // Retrieve next available id for this box
        int nextAvailableId = sharedPref.getInt(key, 1);
        textView.setText(String.format(Locale.US, getString(R.string.placeholder_box_number), nextAvailableId));
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

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Utilities.addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), Utilities.dpToPx(Objects.requireNonNull(getContext()), 150f), Utilities.dpToPx(getContext(), 150f));
        recyclerView.setAdapter(adapter);
    }

    private void setupSwitches(SwitchCompat[] switches) {
        for(SwitchCompat s : switches) {
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(s.getId() == R.id.priority_switch) {
                    viewModel.getBox().setFavorite(isChecked);
                } else {
                    // todo replace with dedicated camera start button
                    buttonView.setChecked(false);
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

//    private void startCameraFromThis() {
//        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            // Permission has already been granted, no need to ask
//            Intent intent = new Intent(getActivity(), CameraActivity.class);
//            startActivityForResult(intent, 1);
//        } else {
//
//            // Permission has not been granted, ask for permission
//            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                    CAMERA_PERMISSION_REQUEST_CODE);
//
//        }
//    }

//    public static int getCameraPermissionRequestCode() {
//        return CAMERA_PERMISSION_REQUEST_CODE;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == getCameraPermissionRequestCode()) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // Permission was granted
//                startCameraFromThis();
//            } else {
//                // Permission was denied
//                // Notify the user the camera functionality will not be available
//                Toast.makeText(getActivity(), "You will not be able to take photos unless you grant permission to use the camera.", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }

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
                saveBoxNumber();
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
                    viewModel.getBox().setContents(paths);
                }
            }
        }
    }

    @Override
    public void startCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, 1);
    }
}
