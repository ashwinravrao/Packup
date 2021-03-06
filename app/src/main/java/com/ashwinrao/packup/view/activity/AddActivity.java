package com.ashwinrao.packup.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
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

import com.ashwinrao.packup.Packup;
import com.ashwinrao.packup.R;
import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.data.model.Item;
import com.ashwinrao.packup.databinding.ActivityAddBinding;
import com.ashwinrao.packup.util.HashtagDetection;
import com.ashwinrao.packup.util.HideShowNotch;
import com.ashwinrao.packup.util.callback.ItemEditedCallback;
import com.ashwinrao.packup.util.callback.SingleItemUnpackCallback;
import com.ashwinrao.packup.view.ConfirmationDialog;
import com.ashwinrao.packup.view.adapter.ItemPackAdapter;
import com.ashwinrao.packup.viewmodel.BoxViewModel;
import com.ashwinrao.packup.viewmodel.InsertionViewModel;
import com.ashwinrao.packup.viewmodel.ItemViewModel;
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

import static com.ashwinrao.packup.util.Decorations.addItemDecoration;
import static com.ashwinrao.packup.util.UnitConversion.dpToPx;

public class AddActivity extends AppCompatActivity implements SingleItemUnpackCallback, ItemEditedCallback {

    private InsertionViewModel insertViewModel;
    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;

    private ActivityAddBinding binding;
    private RecyclerView recyclerView;
    private ItemPackAdapter adapter;
    private String description;
    private List<String> boxCategories = new ArrayList<>();
    private List<Item> items = new ArrayList<>();


    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HideShowNotch.applyThemeIfAvailable(this);
        super.onCreate(savedInstanceState);
        ((Packup) getApplicationContext()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add);

        boxViewModel = new ViewModelProvider(this, factory).get(BoxViewModel.class);
        itemViewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
        insertViewModel = new ViewModelProvider(this, factory).get(InsertionViewModel.class);

        binding.setNumItems(null);
        initializeToolbar(binding.toolbar);
        initializeFields(binding.nameInputField, binding.descriptionInputField);
        initializeRecyclerView(binding.recyclerView);
        initializeButtons(binding.nfcButton, binding.fillButton);

        final Boolean[] notSet = {true};
        boxViewModel.getLastUsedBoxNumber().observe(this, integer -> {
            if (notSet[0]) {
                binding.setBoxNum(integer + 1);
                boxViewModel.getBox().setNumber(integer + 1);
                notSet[0] = false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateItems();
    }

    private void initializeFields(EditText nameInputField, EditText descriptionInputField) {
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
                boxViewModel.getBox().setName(name);
                binding.requiredFieldNotice.setVisibility(s.toString().length() > 0 ? View.GONE : View.VISIBLE);
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
                HashtagDetection.detect(s, boxCategories, matchFound, matchStrings);
            }

            @Override
            public void afterTextChanged(Editable s) {
                tagToChip(s, matchFound, matchStrings, binding.categoryChipGroup);
                description = s.toString().length() > 0 ? s.toString() : "";
                boxViewModel.getBox().setDescription(description);
            }
        });
    }

    private void tagToChip(@NonNull Editable s, @NonNull Boolean[] matchFound, @NonNull String[] matchStrings, @NonNull ChipGroup group) {
        if (matchFound[0]) {
            s.delete(s.length() - matchStrings[0].length(), s.length());
            addNewCategoryChip(group);
        }
    }

    private void addNewCategoryChip(@NonNull ChipGroup group) {
        final Chip chip = new Chip(group.getContext());
        chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setText(boxCategories.get(boxCategories.size() - 1));
        chip.setCloseIconVisible(true);
        chip.setCloseIconTintResource(android.R.color.white);
        chip.setOnCloseIconClickListener(v -> {
            group.removeView(v);
            boxCategories.remove(boxCategories.size() - 1);
        });
        group.addView(chip);
    }

    private void initializeButtons(@NonNull CardView nfcButton, @NonNull CardView fillButton) {
        nfcButton.setOnClickListener(view -> {
            if (boxViewModel.getBox().isTagRegistered()) {
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

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addItemDecoration(this, recyclerView, 1, 8);
        adapter = new ItemPackAdapter(this);
        adapter.setFirstBind(false);
        adapter.setSingleItemUnpackCallback(this);
        adapter.getEditedItem().observe(this, item -> {
            if (item != null) {
                itemViewModel.updateItem(item);
            }
        });
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
    }

    private void initializeToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_overflow_light));
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
                        items.add(new Item(boxViewModel.getBox().getId(), boxViewModel.getBox().getNumber(), path));
                    }
                    itemViewModel.addItemsToThis(items);
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                boxViewModel.getBox().setTagRegistered(true);
                Snackbar.make(binding.snackbarContainer, R.string.tag_success_message, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                        .show();
            }
        }
    }

    private void updateItems() {
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        if (items.size() == 0) {
            binding.setNumItems(null);
        } else {
            binding.setNumItems(items.size() > 1 ? String.format(Locale.US, "%d items", items.size()) : String.format(Locale.US, "%d item", items.size()));
        }
        togglePlaceholderVisibility();
    }

    private void togglePlaceholderVisibility() {
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
        switch (item.getItemId()) {
            case R.id.toolbar_save:
                if (itemViewModel.getItemsFromThis().size() == 0) {
                    showEmptyBoxDialog();
                } else {
                    try {
                        boxViewModel.getBox().setCategories(this.boxCategories);
                        insertViewModel.saveBox(boxViewModel.getBox(), itemViewModel.getItemsFromThis());
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setError(binding.nameInputField);
                    }
                }
                return true;
            case R.id.toolbar_clear_items:
                if (items.size() > 0) {
                    items.clear();
                    updateItems();
                    Snackbar.make(binding.snackbarContainer, "Items cleared", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                            .setAction(getString(R.string.undo), v -> {
                                items.addAll(itemViewModel.getItemsFromThis());
                                updateItems();
                            })
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                        itemViewModel.clearItemsFromThis();
                                    }
                                }
                            }).show();
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
                text.setHintTextColor(ContextCompat.getColor(AddActivity.this, R.color.top_row_view_holder));
                text.setBackgroundTintList(getColorStateList(R.color.state_list_input_field));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void showUnsavedChangesDialog() {
        ConfirmationDialog.INSTANCE.make(this, new String[]{
                getString(R.string.dialog_discard_box_title),
                getString(R.string.dialog_discard_box_message),
                getString(R.string.discard),
                getString(R.string.cancel)}, true, new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, android.R.color.holo_red_dark)}, dialogInterface -> {
            if (itemViewModel.getItemsFromThis() != null) {
                for (Item item : itemViewModel.getItemsFromThis()) {
                    new File(Objects.requireNonNull(item.getFilePath())).delete();
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
        final AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_empty_box_title)
                .setMessage(R.string.dialog_empty_box_message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void showNfcTagAlreadyRegisteredDialog() {
        ConfirmationDialog.INSTANCE.make(this, new String[]{
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
        if (box.getName() != null || items.size() > 0) {
            showUnsavedChangesDialog();
        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        closeWithConfirmation();
    }

    @Override
    public void unpackItem(@NonNull Item item, int position) {
        if (items.size() > 0) {
            items.remove(item);
            updateItems();
        }

        Snackbar.make(binding.snackbarContainer, "Item removed", Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                .setAction(getString(R.string.undo), v -> {
                    items.add(position, item);
                    updateItems();
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            itemViewModel.removeItemFromThis(item);
                        }
                    }
                })
                .show();
    }

    @Override
    public void itemEdited(@NonNull Item item, int position) {
        itemViewModel.updateItem(item);
    }
}
