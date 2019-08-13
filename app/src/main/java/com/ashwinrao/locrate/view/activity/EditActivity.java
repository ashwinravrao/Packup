package com.ashwinrao.locrate.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ActivityEditBinding;
import com.ashwinrao.locrate.util.callback.ItemEditedCallback;
import com.ashwinrao.locrate.view.adapter.ItemPackAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.CategoryViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class EditActivity extends AppCompatActivity implements ItemEditedCallback {

    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;
    private CategoryViewModel categoryViewModel;

    private Box thisBox;
    private final boolean[] initialBoxBound = {false};
    private ActivityEditBinding binding;
    private RecyclerView recyclerView;
    private ItemPackAdapter adapter;
    private String description;
    private boolean wasTagAssigned = false;
    private List<Item> items = new ArrayList<>();

    private static final String TAG = "EditActivity";


    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Locrate) getApplicationContext()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        boxViewModel = ViewModelProviders.of(this, factory).get(BoxViewModel.class);
        itemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);
//        categoryViewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel.class);

        // layout widgets
        setupToolbar(binding.toolbar);
        setupInputFields(binding.nameInputField, binding.descriptionInputField);
        setupRecyclerView(binding.recyclerView);
//        setupButtons(binding.nfcButton, binding.fillButton);

        final String uuid = Objects.requireNonNull(getIntent().getExtras()).getString("ID");

        boxViewModel.getBoxByUUID(uuid).observe(this, box -> {
            if (box != null) {
                populate(box);
                boxViewModel.getBoxByUUID(uuid).removeObservers(this);
            }
        });

        itemViewModel.getItemsFromBox(uuid).observe(this, items -> {
            if(items != null) {
                populateItems(items);
            }
        });

//        itemViewModel.getItemsFromBox(uuid).observe(this, items ->
//                itemViewModel.addItemsToThis(items));

//        // Setup CategoryViewModel to be able to retrieve item categories later
//        new Handler().post(() ->
//                itemViewModel.getAllItemsFromDatabase().observe(this, items ->
//                        categoryViewModel.setCachedItemCategories(items)));
//
//        // data binding
//        binding.setNumItems(String.format(getString(R.string.number_of_items_format_string), itemViewModel.getItemsFromThis().size()));
//        binding.setBoxNum(thisBox.getNumber());
//
//        // layout widgets
//        setupToolbar(binding.toolbar);
//        setupInputFields(binding.nameInputField, binding.descriptionInputField);
//        setupRecyclerView(binding.recyclerView);
//        setupButtons(binding.nfcButton, binding.fillButton);
    }

    private void populate(@NonNull final Box box) {
        thisBox = box;
        binding.setBox(thisBox);
        initialBoxBound[0] = true;
    }

    private void populateItems(@NonNull final List<Item> items) {
        this.items = items;
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        adapter.setFirstBind(false);
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        updateItems();
//    }
//
    private void setupInputFields(EditText nameInputField, EditText descriptionInputField) {

//        nameInputField.setText(thisBox.getName());
//        descriptionInputField.setText(thisBox.getDescription());
//        for(String category : thisBox.getCategories()) {
//            addPreExistingCategoryChips(binding.categoryChipGroup, category);
//        }

//        final Boolean[] matchFound = {false};
//        final String[] matchStrings = {null, null};

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
                    thisBox.setName(name);
                    boxViewModel.updateBox(thisBox);
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
//                HashtagDetection.detect(s, thisBox.getCategories(), matchFound, matchStrings);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                tagToChip(s, matchFound, matchStrings, binding.categoryChipGroup);
                description = s.toString().length() > 0 ? s.toString() : "";
                if (initialBoxBound[0]) {
                    thisBox.setDescription(description);
                    boxViewModel.updateBox(thisBox);
                }
            }
        });
    }

    //
//    private void tagToChip(@NonNull Editable s, @NonNull Boolean[] matchFound, @NonNull String[] matchStrings, @NonNull ChipGroup group) {
//        if (matchFound[0]) {
//            s.delete(s.length() - matchStrings[0].length(), s.length());
//            addNewCategoryChip(group);
//        }
//    }
//
//    private void addPreExistingCategoryChips(@NonNull final ChipGroup group, @NonNull final String text) {
//        final Chip chip = new Chip(group.getContext());
//        chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
//        chip.setChipBackgroundColorResource(R.color.colorAccent);
//        chip.setText(text);
//        chip.setCloseIconVisible(true);
//        chip.setCloseIconTintResource(android.R.color.white);
//        chip.setOnCloseIconClickListener(v -> {
//            group.removeView(v);
//            thisBox.getCategories().remove(text);
//        });
//        group.addView(chip);
//    }
//
//    private void addNewCategoryChip(@NonNull ChipGroup group) {
//        final Chip chip = new Chip(group.getContext());
//        chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
//        chip.setChipBackgroundColorResource(R.color.colorAccent);
//        chip.setText(thisBox.getCategories().get(thisBox.getCategories().size() - 1));
//        chip.setCloseIconVisible(true);
//        chip.setCloseIconTintResource(android.R.color.white);
//        chip.setOnCloseIconClickListener(v -> {
//            group.removeView(v);
//            thisBox.getCategories().remove(thisBox.getCategories().size() - 1);
//        });
//        group.addView(chip);
//    }
//
//    private void setupButtons(@NonNull CardView nfcButton, @NonNull CardView fillButton) {
//        nfcButton.setOnClickListener(view -> {
//            if (wasTagAssigned) {
//                showNfcTagAlreadyRegisteredDialog();
//            } else {
//                createNfcTagRegistrationIntent();
//            }
//        });
//        fillButton.setOnClickListener(view -> startCamera());
//    }
//
//    private void createNfcTagRegistrationIntent() {
//        final Intent intent = new Intent(this, NfcActivity.class);
//        intent.putExtra("isWrite", true);
//        intent.putExtra("uuidToRegister", boxViewModel.getBox().getId());
//        startActivityForResult(intent, 2);
//    }
//
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        addItemDecoration(this, recyclerView, 1);
        adapter = new ItemPackAdapter(this);
        adapter.setItemEditedCallback(this);
//        adapter.getEditedItem().observe(this, item -> {
//            if (item != null) {
//                itemViewModel.updateItem(item);
//            }
//        });
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_overflow_light));
//        toolbar.setNavigationOnClickListener(v -> closeWithConfirmation());
    }

    @Override
    public void itemEdited(@NonNull Item item, @NonNull Integer position) {
        this.items.remove((int) position);
        this.items.add(position, item);
//        itemViewModel.updateItem(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        itemViewModel.updateItems(items);
    }

    //
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                final ArrayList<String> paths = Objects.requireNonNull(data).getStringArrayListExtra("paths");
//                if (paths != null) {
//                    for (String path : paths) {
//                        items.add(new Item(boxViewModel.getBox().getId(), boxViewModel.getBox().getNumber(), path));
//                    }
//                    itemViewModel.addItemsToThis(items);
//                }
//            }
//        }
//
//        if (requestCode == 2) {
//            if (resultCode == RESULT_OK) {
//                wasTagAssigned = true;
//                Snackbar.make(binding.snackbarContainer, R.string.tag_success_message, Snackbar.LENGTH_SHORT)
//                        .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
//                        .show();
//            }
//        }
//    }
//
//    private void updateItems() {
//        adapter.setItems(items);
//        recyclerView.setAdapter(adapter);
//        if (items.size() == 0) binding.setNumItems(null);
//        else
//            binding.setNumItems(items.size() > 1 ? String.format(Locale.US, "%d items", items.size()) : String.format(Locale.US, "%d item", items.size()));
//        togglePlaceholderVisibility();
//    }
//
//    private void togglePlaceholderVisibility() {
//        final View[] placeholders = new View[]{binding.placeholderImage, binding.placeholderText};
//        for (View v : placeholders) {
//            if (items != null) {
//                v.setVisibility(items.size() > 0 ? View.GONE : View.VISIBLE);
//            } else {
//                v.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    private void startCamera() {
//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_add, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.toolbar_save:
//                if (itemViewModel.getItemsFromThis().size() == 0) {
//                    showEmptyBoxDialog();
//                } else {
//                    if (boxViewModel.saveBox(this.thisBox.getCategories())) {
//                        itemViewModel.insertItems(itemViewModel.getItemsFromThis());
//                        this.finish();
//                    } else {
//                        setError(binding.nameInputField);
//                    }
//                }
//                return true;
//            case R.id.toolbar_clear_items:
//                if (items.size() > 0) {
//                    items.clear();
//                    updateItems();
//                    Snackbar.make(binding.snackbarContainer, "Items cleared", Snackbar.LENGTH_LONG)
//                            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
//                            .setAction(getString(R.string.undo), v -> {
//                                items.addAll(itemViewModel.getItemsFromThis());
//                                updateItems();
//                            })
//                            .addCallback(new Snackbar.Callback() {
//                                @Override
//                                public void onDismissed(Snackbar transientBottomBar, int event) {
//                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
//                                        itemViewModel.clearItemsFromThis();
//                                    }
//                                }
//                            }).show();
//                } else {
//                    final Toast toast = Toast.makeText(this, "Nothing to clear", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM, 0, dpToPx(this, 112f));
//                    toast.show();
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void setError(@NonNull EditText text) {
//        text.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.delete_red)));
//        text.setHintTextColor(ContextCompat.getColor(this, R.color.delete_red));
//        text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                text.setHintTextColor(ContextCompat.getColor(EditActivity.this, R.color.top_row_view_holder));
//                text.setBackgroundTintList(getColorStateList(R.color.state_list_input_field));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//    }
//
//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private void showUnsavedChangesDialog() {
//        ConfirmationDialog.make(this, new String[]{
//                getString(R.string.dialog_discard_box_title),
//                getString(R.string.dialog_discard_box_message),
//                getString(R.string.discard),
//                getString(R.string.cancel)}, true, new int[]{ContextCompat.getColor(this, R.color.colorAccent),
//                ContextCompat.getColor(this, android.R.color.holo_red_dark)}, dialogInterface -> {
//            if (itemViewModel.getItemsFromThis() != null) {
//                for (Item item : itemViewModel.getItemsFromThis()) {
//                    new File(item.getFilePath()).delete();
//                }
//                itemViewModel.deleteItems(itemViewModel.getItemsFromThis());
//            }
//            this.finish();
//            return null;
//        }, dialogInterface -> {
//            dialogInterface.cancel();
//            return null;
//        });
//    }
//
//    private void showEmptyBoxDialog() {
//        final AlertDialog dialog = new MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.dialog_empty_box_title)
//                .setMessage(R.string.dialog_empty_box_message)
//                .setCancelable(true)
//                .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss()).create();
//        dialog.show();
//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
//    }
//
//    private void showNfcTagAlreadyRegisteredDialog() {
//        ConfirmationDialog.make(this, new String[]{
//                        getString(R.string.dialog_nfc_tag_already_registered_title),
//                        getString(R.string.dialog_nfc_tag_already_registered_message),
//                        getString(R.string.yes),
//                        getString(R.string.no)},
//                true,
//                new int[]{ContextCompat.getColor(this, R.color.colorAccent),
//                        ContextCompat.getColor(this, R.color.colorAccent)},
//                dialogInterface -> {
//                    createNfcTagRegistrationIntent();
//                    return null;
//                }, dialogInterface -> {
//                    dialogInterface.cancel();
//                    return null;
//                });
//    }
//
//    private void closeWithConfirmation() {
//        final Box box = boxViewModel.getBox();
//        if (box.getName() != null || items.size() > 0) {
//            showUnsavedChangesDialog();
//        } else {
//            this.finish();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        closeWithConfirmation();
//    }
//
//    @Override
//    public void unpackItem(@NonNull Item item, @NonNull Integer position) {
//        if (items.size() > 0) {
//            items.remove(item);
//            updateItems();
//        }
//
//        Snackbar.make(binding.snackbarContainer, "Item removed", Snackbar.LENGTH_LONG)
//                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
//                .setAction(getString(R.string.undo), v -> {
//                    items.add(position, item);
//                    updateItems();
//                })
//                .addCallback(new Snackbar.Callback() {
//                    @Override
//                    public void onDismissed(Snackbar transientBottomBar, int event) {
//                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
//                            itemViewModel.removeItemFromThis(item);
//                        }
//                    }
//                })
//                .show();
//    }
}
