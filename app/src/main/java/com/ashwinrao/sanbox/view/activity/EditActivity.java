package com.ashwinrao.sanbox.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.sanbox.Sanbox;
import com.ashwinrao.sanbox.R;
import com.ashwinrao.sanbox.data.model.Box;
import com.ashwinrao.sanbox.data.model.Item;
import com.ashwinrao.sanbox.databinding.ActivityEditBinding;
import com.ashwinrao.sanbox.util.HashtagDetection;
import com.ashwinrao.sanbox.util.callback.ItemEditedCallback;
import com.ashwinrao.sanbox.util.callback.SingleItemUnpackCallback;
import com.ashwinrao.sanbox.view.ConfirmationDialog;
import com.ashwinrao.sanbox.view.adapter.ItemPackAdapter;
import com.ashwinrao.sanbox.viewmodel.BoxViewModel;
import com.ashwinrao.sanbox.viewmodel.CategoryViewModel;
import com.ashwinrao.sanbox.viewmodel.ItemViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.sanbox.util.Decorations.addItemDecoration;
import static com.ashwinrao.sanbox.util.UnitConversion.dpToPx;

public class EditActivity extends AppCompatActivity implements ItemEditedCallback, SingleItemUnpackCallback {

    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;
    private CategoryViewModel categoryViewModel;

    private Box edited;
    private final boolean[] initialBoxBound = {false};
    private ActivityEditBinding binding;
    private RecyclerView recyclerView;
    private ItemPackAdapter adapter;
    private String description;
    private List<Item> items = new ArrayList<>();
    private List<String> addItemPaths = new ArrayList<>();

    private static final String TAG = "EditActivity";


    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Sanbox) getApplicationContext()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        boxViewModel = new ViewModelProvider(this, factory).get(BoxViewModel.class);
        itemViewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
        categoryViewModel = new ViewModelProvider(this, factory).get(CategoryViewModel.class);

        // Setup CategoryViewModel to be able to retrieve item categories later
        // Used for suggesting previously made categories (app-wide) in AutoCompleteTextView later on
        new Handler().post(() ->
                itemViewModel.getAllItemsFromDatabase().observe(this, items ->
                        categoryViewModel.setCachedItemCategories(items)));

        setupToolbar(binding.toolbar);
        setupInputFields(binding.nameInputField, binding.descriptionInputField);
        setupRecyclerView(binding.recyclerView);
        setupButtons(binding.nfcButton, binding.fillButton);

        final String uuid = Objects.requireNonNull(getIntent().getExtras()).getString("ID");

        boxViewModel.getBoxByUUID(uuid).observe(this, box -> {
            if (box != null) {
                populate(box);
                boxViewModel.getBoxByUUID(uuid).removeObservers(this);
            }
        });

        itemViewModel.getItemsFromBox(uuid).observe(this, items -> {
            if (items != null) {
                populateItems(items);
                itemViewModel.getItemsFromBox(uuid).removeObservers(this);
            }
        });
    }

    private void populate(@NonNull final Box box) {
        edited = box;
        binding.setBox(edited);
        populateCategories(edited.getCategories());
        initialBoxBound[0] = true;
    }

    private void populateCategories(@NonNull List<String> categories) {
        for(String category : categories) {
            addNewCategoryChip(binding.categoryChipGroup, category);
        }
    }

    private void populateItems(@NonNull final List<Item> items) {
        this.items = items;
        adapter.setFirstBind(false);
        updateListedItems();
    }

    private void setupInputFields(EditText nameInputField, EditText descriptionInputField) {

        final Boolean[] matchFound = {false};
        final String[] matchStrings = {null, null};

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
                binding.requiredFieldNotice.setVisibility(s.toString().length() > 0 ? View.GONE : View.VISIBLE);
                if (initialBoxBound[0]) {
                    edited.setName(name);
                }
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
                HashtagDetection.detect(s, edited.getCategories(), matchFound, matchStrings);
            }

            @Override
            public void afterTextChanged(Editable s) {
                description = s.toString().length() > 0 ? s.toString() : "";
                if (initialBoxBound[0]) {
                    edited.setDescription(description);
                    tagToChip(s, matchFound, matchStrings, binding.categoryChipGroup);
                }
            }
        });
    }


    private void tagToChip(@NonNull Editable s, @NonNull Boolean[] matchFound, @NonNull String[] matchStrings, @NonNull ChipGroup group) {
        if (matchFound[0]) {
            s.delete(s.length() - matchStrings[0].length(), s.length());
            addNewCategoryChip(group, null);
        }
    }

    private void addNewCategoryChip(@NonNull ChipGroup group, @Nullable final String text) {
        final Chip chip = new Chip(group.getContext());
        chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setText(text == null ? edited.getCategories().get(edited.getCategories().size() - 1) : text);
        chip.setCloseIconVisible(true);
        chip.setCloseIconTintResource(android.R.color.white);
        chip.setOnCloseIconClickListener(v -> {
            group.removeView(v);
            edited.getCategories().remove(text == null ? edited.getCategories().size() - 1 : text);
        });
        group.addView(chip);
    }

    private void setupButtons(@NonNull CardView nfcButton, @NonNull CardView fillButton) {
        nfcButton.setOnClickListener(view -> {
            if (edited.isTagRegistered()) {
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        addItemDecoration(this, recyclerView, 1);
        adapter = new ItemPackAdapter(this);
        adapter.setItemEditedCallback(this);
        adapter.setSingleItemUnpackCallback(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_overflow_light));
        toolbar.setNavigationOnClickListener(v -> closeWithConfirmation());
    }

    private void saveAndClose() {
        if (items.size() > 0) {
            boxViewModel.updateBox(edited);
            itemViewModel.insertItems(this.items);
            this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final ArrayList<String> paths = Objects.requireNonNull(data).getStringArrayListExtra("paths");
                if (paths != null) {
                    addItemPaths.addAll(paths);
                    for (String path : paths) {
                        this.items.add(new Item(edited.getId(), edited.getNumber(), path));
                    }
                    updateListedItems();
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                edited.setTagRegistered(true);
                Snackbar.make(binding.snackbarContainer, R.string.tag_success_message, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                        .show();
            }
        }
    }

    private void updateListedItems() {
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        if (items.size() == 0) {
            binding.setNumItems(null);
        } else {
            binding.setNumItems(items.size() > 1 ?
                    String.format(Locale.US, "%d items", items.size()) :
                    String.format(Locale.US, "%d item", items.size()));
        }
        togglePlaceholderVisibility(items);
    }

    private void clearItems() {
        this.items.clear();
        updateListedItems();
    }

    private void removeItem(@NonNull Item item) {
        this.items.remove(item);
        updateListedItems();
    }

    private void restoreItems(@NonNull List<Item> items) {
        if (items.size() > 0) {
            this.items.addAll(items);
            updateListedItems();
        }
    }

    private void restoreSingleItem(@NonNull Item item) {
        this.items.add(item);
        updateListedItems();
    }

    private void togglePlaceholderVisibility(@Nullable List<Item> items) {
        final View[] placeholders = new View[]{binding.placeholderImage, binding.placeholderText};
        for (View v : placeholders) {
            if (items != null) {
                v.setVisibility(items.size() > 0 ? View.GONE : View.VISIBLE);
            } else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startCamera() {
        final Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save:
                if (items.size() == 0) {
                    showEmptyBoxDialog();
                } else {
                    if (edited.getName() != null) {
                        saveAndClose();
                    } else {
                        setError(binding.nameInputField);
                    }
                }
                return true;
            case R.id.toolbar_clear_items:
                if (items.size() > 0) {
                    final List<Item> itemsCopy = new ArrayList<>(items);
                    clearItems();
                    Snackbar.make(binding.snackbarContainer, "Items cleared", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                            .setAction(getString(R.string.undo), v -> restoreItems(itemsCopy))
                            .show();
                } else {
                    final Toast toast = Toast.makeText(this, "Nothing to clear", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, dpToPx(this, 112f));
                    toast.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setError(@NonNull EditText text) {
        text.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.delete_red)));
        text.setHintTextColor(ContextCompat.getColor(this, R.color.delete_red));
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text.setHintTextColor(ContextCompat.getColor(EditActivity.this, R.color.top_row_view_holder));
                text.setBackgroundTintList(getColorStateList(R.color.state_list_input_field));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void showPossibleUnsavedChangesDialog() {
        ConfirmationDialog.make(this, new String[]{
                getString(R.string.dialog_discard_changes_title),
                getString(R.string.dialog_discard_changes_message),
                getString(R.string.leave),
                getString(R.string.cancel)}, true, new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.delete_red)}, dialogInterface -> {
            if (addItemPaths.size() > 0) for (String path : addItemPaths) new File(path).delete();
            this.finish();
            return null;
        }, dialogInterface -> {
            dialogInterface.cancel();
            return null;
        });
    }

    private void showEmptyBoxDialog() {
        final AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_empty_box_title)
                .setMessage(R.string.dialog_empty_box_message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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
        showPossibleUnsavedChangesDialog();
    }

    @Override
    public void onBackPressed() {
        closeWithConfirmation();
    }

    @Override
    public void itemEdited(@NonNull Item item, @NonNull Integer position) {
        this.items.remove((int) position);
        this.items.add(position, item);
    }

    @Override
    public void unpackItem(@NonNull Item item, @NonNull Integer position) {
        removeItem(item);
        Snackbar.make(binding.snackbarContainer, "Item removed", Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                .setAction(getString(R.string.undo), v -> restoreSingleItem(item))
                .show();
    }
}
