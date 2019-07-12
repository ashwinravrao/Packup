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
import android.widget.Toast;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.FragmentAddBinding;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.CameraActivity;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class AddFragment extends Fragment implements Toolbar.OnMenuItemClickListener, BackNavCallback {

    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;
    private FragmentAddBinding binding;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> items = new ArrayList<>();

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
        boxViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        itemViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);

        // data binding
        binding.setBoxId(getBoxNumber());

        // layout widgets
        initializeToolbar(binding.toolbar);
        initializeFields(binding.nameInputField, binding.descriptionInputField);
        initializeRecyclerView(binding.recyclerView);
        initializeButtons(binding.nfcButton, binding.fillButton);

        return binding.getRoot();
    }

    private void initializeFields(EditText nameInputField, EditText descriptionInputField) {

        // Name Input Field
        Objects.requireNonNull(nameInputField).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String name = s.toString().length() > 0 ? s.toString() : null;
                boxViewModel.getBox().setName(name);
            }
        });

        // Description Input Field
        Objects.requireNonNull(descriptionInputField).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.charCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String description = s.toString().length() > 0 ? s.toString() : null;
                boxViewModel.getBox().setDescription(description);
            }
        });
    }

    private void initializeButtons(@NonNull MaterialCardView nfcButton, @NonNull MaterialCardView fillButton) {
        nfcButton.setOnClickListener(view -> Toast.makeText(getContext(), "Provisioning NFC tag...", Toast.LENGTH_SHORT).show());
        fillButton.setOnClickListener(view -> startCamera());
    }

    private SharedPreferences getSharedPreferences(@NonNull Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    private int getBoxNumber() {
        // Retrieve next available id
        int lastUsed = getSharedPreferences(Objects.requireNonNull(getActivity())).getInt(PREF_ID_KEY, 0);
        boxViewModel.getBox().setId(lastUsed + 1);
        return lastUsed + 1;
    }

    private void saveBoxNumber() {
        SharedPreferences sharedPref = getSharedPreferences(Objects.requireNonNull(getActivity()));
        int nextAvailableId = sharedPref.getInt(PREF_ID_KEY, 1);

        // Store next available id
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_ID_KEY, nextAvailableId + 1);
        editor.apply();  // .apply() >= .commit()
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getActivity()), true, true);
        itemsAdapter.getRenamedItem().observe(this, item -> {
            if (item != null) itemViewModel.updateItem(item);
        });
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
    }

    private void initializeToolbar(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> closeWithConfirmation());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_done) {
            if (itemViewModel.getItemsFromThis().size() == 0) {
                showEmptyBoxDialog();
                return true;
            } else {
                if (boxViewModel.saveBox()) {
                    saveBoxNumber();
                    itemViewModel.insertItems(itemViewModel.getItemsFromThis());
                    Objects.requireNonNull(getActivity()).finish();
                    return true;
                } else {
                    Objects.requireNonNull(binding.nameInputField).setError(getResources().getString(R.string.name_field_error_message));
                    binding.nameInputField.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            binding.nameInputField.setError(null);
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
                    for (String path : paths) {
                        items.add(new Item(getBoxNumber(), path));
                    }
                    itemViewModel.setItems(items);
                }
            }
        }
    }

    private void updateItems() {
        if (items.size() > 0) {
            itemsAdapter.setItems(items);
            recyclerView.setAdapter(itemsAdapter);
            binding.setNumItems(itemViewModel.getItemsFromThis().size());
        }
    }

    private void startCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    private void closeWithConfirmation() {
        final Box box = boxViewModel.getBox();
        if (box.getName() != null || !box.getDescription().equals("No description") || items.size() > 0) {
            showUnsavedChangesDialog();
        } else {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void showUnsavedChangesDialog() {
        ConfirmationDialog.make(getContext(), new String[]{
                getString(R.string.dialog_discard_box_title),
                getString(R.string.dialog_discard_box_message),
                getString(R.string.discard),
                getString(R.string.cancel)}, true, new int[]{ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent),
                ContextCompat.getColor(Objects.requireNonNull(getContext()), android.R.color.holo_red_dark)}, dialogInterface -> {
            if (itemViewModel.getItemsFromThis() != null) {
                for (Item item : itemViewModel.getItemsFromThis()) {
                    new File(item.getFilePath()).delete();
                }
                itemViewModel.deleteItems(itemViewModel.getItemsFromThis());
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
                true,
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
