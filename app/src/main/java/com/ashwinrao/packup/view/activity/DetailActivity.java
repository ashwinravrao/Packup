package com.ashwinrao.packup.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.packup.Packup;
import com.ashwinrao.packup.R;
import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.data.model.Item;
import com.ashwinrao.packup.databinding.ActivityDetailBinding;
import com.ashwinrao.packup.view.ConfirmationDialog;
import com.ashwinrao.packup.view.adapter.ItemDisplayAdapter;
import com.ashwinrao.packup.viewmodel.BoxViewModel;
import com.ashwinrao.packup.viewmodel.ItemViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import static com.ashwinrao.packup.util.Decorations.addItemDecoration;
import static com.ashwinrao.packup.util.UnitConversion.dpToPx;

public class DetailActivity extends AppCompatActivity {

    private Box box;
    private ItemDisplayAdapter adapter;
    private BoxViewModel boxViewModel;
    private LiveData<Box> boxLD;
    private LiveData<List<Item>> itemsLD;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Packup) getApplicationContext()).getAppComponent().inject(this);

        // Exclude certain view elements from participating in activity fade transition
        final Fade fade = new Fade();
        final View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        boxViewModel = new ViewModelProvider(this, factory).get(BoxViewModel.class);
        final ItemViewModel itemViewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);

        final Bundle extras = getIntent().getExtras();
        if(extras != null) {
            final String boxUUID = extras.getString("ID");
            if(boxUUID != null) {
                boxLD = boxViewModel.getBoxByUUID(boxUUID);
                itemsLD = itemViewModel.getItemsFromBox(boxUUID);
            } else {
                supportFinishAfterTransition();
            }
        } else {
            supportFinishAfterTransition();
        }

        setupToolbar(binding.toolbar);
        setupRecyclerView(binding, binding.recyclerView);
        setupButtons(binding.editButton, binding.deleteButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null) {
            adapter.initializeFilter();
        }
    }

    private void setupButtons(@NonNull FloatingActionButton editButton, @NonNull FloatingActionButton deleteButton) {
        editButton.setOnClickListener(view -> {
            final Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("ID", box.getId());
            startActivity(intent);
        });

        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void setupToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> supportFinishAfterTransition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_list, menu);
        final MenuItem search = menu.findItem(R.id.toolbar_search);
        final SearchView searchView = (SearchView) search.getActionView();
        configureSearchView(searchView);
        return true;
    }

    private void configureSearchView(SearchView searchView) {
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search boxes");
        searchView.setPadding(dpToPx(this, -16f), 0, 0, dpToPx(this, -1f));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setupRecyclerView(@NonNull ActivityDetailBinding binding, @NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        addItemDecoration(this, recyclerView, 1, 8);
        adapter = new ItemDisplayAdapter(this, true);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        boxLD.observe(this, box -> {
            this.box = box;
            binding.setBox(box);
            setupCategoryChips(binding.chipGroup, box.getCategories());
        });
        itemsLD.observe(this, items -> {
            adapter.submitList(items);
            adapter.setItemsForFiltering(items);
            setNumberOfItems(binding.numberOfItems, items.size());
        });
    }

    private void setupCategoryChips(@NonNull ChipGroup chipGroup, @NonNull List<String> categories) {
        if(categories.size() > 0) {
            for (String category : categories) {
                addNewCategoryChip(chipGroup, category);
            }
        }
    }

    private void addNewCategoryChip(@NonNull ChipGroup group, @Nullable final String text) {
        final Chip chip = new Chip(group.getContext());
        chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setText(text == null ? box.getCategories().get(box.getCategories().size() - 1) : text);
        chip.setClickable(false);
        group.addView(chip);
    }

    private void setNumberOfItems(TextView textView, final int numberOfItems) {
        textView.setText(numberOfItems == 1
                ? getString(R.string.one_item)
                : String.format(getString(R.string.number_of_items_format_string), numberOfItems));
    }

    private void showDeleteConfirmationDialog() {
        ConfirmationDialog.make(this,
                new String[]{
                        getString(R.string.dialog_delete_existing_box_title),
                        getString(R.string.dialog_delete_existing_box_message),
                        getString(R.string.delete),
                        getString(R.string.no)},
                true,
                new int[]{
                        ContextCompat.getColor(this, R.color.colorAccent),
                        ContextCompat.getColor(this, android.R.color.holo_red_dark)},
                dialogInterface -> {
                    boxViewModel.delete(box);
                    supportFinishAfterTransition();
                    return null;
                }, dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
