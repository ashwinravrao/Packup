package com.ashwinrao.locrate.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ActivityAddBinding;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class AddActivity extends AppCompatActivity {

    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;
    private ActivityAddBinding binding;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private boolean wasTagAssigned = false;
    private List<Item> items = new ArrayList<>();

    private final String PREF_ID_KEY = "next_available_id";
    private static final String TAG = "AddActivity";

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Locrate) getApplicationContext()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add);

        boxViewModel = ViewModelProviders.of(this, factory).get(BoxViewModel.class);
        itemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);

        // data binding
        binding.setBoxNum(getBoxNumber());

        // layout widgets
        initializeToolbar(binding.toolbar);
        initializeFields(binding.nameInputField, binding.descriptionInputField);
        initializeRecyclerView(binding.recyclerView);
        initializeButtons(binding.nfcButton, binding.fillButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateItems();
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
        nfcButton.setOnClickListener(view -> {
            if (wasTagAssigned) {
                showNfcTagAlreadyRegisteredDialog();
            } else {
                createNfcTagRegistrationIntent();
            }
        });
        fillButton.setOnClickListener(view -> startCamera());
    }

    private void createNfcTagRegistrationIntent() {
        final Intent intent = new Intent(this, NfcActivity.class);
        intent.putExtra("isWrite", true);
        intent.putExtra("uuidToRegister", boxViewModel.getBox().getId());
        startActivityForResult(intent, 2);
    }

    private SharedPreferences getSharedPreferences(@NonNull Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    private int getBoxNumber() {
        // Retrieve next available id
        int lastUsed = getSharedPreferences(this).getInt(PREF_ID_KEY, 0);
        boxViewModel.getBox().setNumber(lastUsed + 1);
        return lastUsed + 1;
    }

    private void saveBoxNumber() {
        SharedPreferences sharedPref = getSharedPreferences(this);
        int nextAvailableId = sharedPref.getInt(PREF_ID_KEY, 1);

        // Store next available id
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_ID_KEY, nextAvailableId + 1);
        editor.apply();  // .apply() >= .commit()
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addItemDecoration(this, recyclerView, 1);
        itemsAdapter = new ItemsAdapter(this, true, true);
        itemsAdapter.getRenamedItem().observe(this, item -> {
            if (item != null) itemViewModel.updateItem(item);
        });
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
    }

    private void initializeToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> closeWithConfirmation());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final ArrayList<String> paths = Objects.requireNonNull(data).getStringArrayListExtra("paths");
                if (paths != null) {
                    for (String path : paths) {
                        items.add(new Item(boxViewModel.getBox().getId(), this.getBoxNumber(), path));
                    }
                    itemViewModel.setItems(items);
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                wasTagAssigned = true;
                Toast toast = Toast.makeText(this, R.string.tag_success_message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, dpToPx(this, 94f));
                toast.show();
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
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_done) {
            if (itemViewModel.getItemsFromThis().size() == 0) {
                showEmptyBoxDialog();
                return true;
            } else {
                if (boxViewModel.saveBox()) {
                    saveBoxNumber();
                    itemViewModel.insertItems(itemViewModel.getItemsFromThis());
                    this.finish();
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
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void showUnsavedChangesDialog() {
        ConfirmationDialog.make(this, new String[]{
                getString(R.string.dialog_discard_box_title),
                getString(R.string.dialog_discard_box_message),
                getString(R.string.discard),
                getString(R.string.cancel)}, true, new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, android.R.color.holo_red_dark)}, dialogInterface -> {
            if (itemViewModel.getItemsFromThis() != null) {
                for (Item item : itemViewModel.getItemsFromThis()) {
                    new File(item.getFilePath()).delete();
                }
                itemViewModel.deleteItems(itemViewModel.getItemsFromThis());
            }
            this.finish();
            return null;
        }, dialogInterface -> {
            dialogInterface.cancel();
            return null;
        });
    }

    private void showEmptyBoxDialog() {
        ConfirmationDialog.make(this, new String[]{
                        getString(R.string.dialog_empty_box_title),
                        getString(R.string.dialog_empty_box_message),
                        getString(R.string.ok),
                        getString(R.string.discard)},
                true,
                new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                        ContextCompat.getColor(this, R.color.colorAccent)},
                dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                }, dialogInterface -> {
                    this.finish();
                    return null;
                });
    }

    private void showNfcTagAlreadyRegisteredDialog() {
        ConfirmationDialog.make(this, new String[]{
                        getString(R.string.dialog_nfc_tag_already_registered_title),
                        getString(R.string.dialog_nfc_tag_already_registered_message),
                        getString(R.string.yes),
                        getString(R.string.no)},
                true,
                new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                        ContextCompat.getColor(this, R.color.colorAccent)},
                dialogInterface -> {
                    createNfcTagRegistrationIntent();
                    return null;
                }, dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                });
    }

    private void closeWithConfirmation() {
        final Box box = boxViewModel.getBox();
        if (box.getName() != null || !box.getDescription().equals("No description") || items.size() > 0) {
            showUnsavedChangesDialog();
        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        closeWithConfirmation();
    }
}
