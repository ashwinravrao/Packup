package com.ashwinrao.locrate.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ActivityDetailBinding;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class DetailActivity extends AppCompatActivity {

    private Box box;
    private ItemsAdapter itemsAdapter;
    private BoxViewModel boxViewModel;
    private LiveData<Box> boxLD;
    private LiveData<List<Item>> itemsLD;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Locrate) getApplicationContext()).getAppComponent().inject(this);
        final ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        boxViewModel = ViewModelProviders.of(this, factory).get(BoxViewModel.class);
        final ItemViewModel itemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);

        final Bundle extras = getIntent().getExtras();
        if(extras != null) {
            final int boxId = extras.getInt("ID", 0);
            boxLD = boxViewModel.getBoxByID(boxId);
            itemsLD = itemViewModel.getItemsFromBox(boxId);
        } else {
            finishWithTransition();
        }

        // layout widgets
        initializeToolbar(binding.toolbar);
        initializeRecyclerView(binding, binding.recyclerView);
        initializeButtons(binding.editButton, binding.deleteButton);
    }

    private void initializeButtons(@NonNull FloatingActionButton editButton, @NonNull FloatingActionButton deleteButton) {
        editButton.setOnClickListener(view -> {

        });

        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void initializeToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finishWithTransition());
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
                itemsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initializeRecyclerView(@NonNull ActivityDetailBinding binding, @NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addItemDecoration(this, recyclerView, 1);
        itemsAdapter = new ItemsAdapter(this, true, false);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
        boxLD.observe(this, box -> {
            this.box = box;
            binding.setBox(box);
        });
        itemsLD.observe(this, items -> {
            itemsAdapter.setItems(items);
            binding.setNumItems(items.size());
            recyclerView.setAdapter(itemsAdapter);
        });
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
                    finishWithTransition();
                    return null;
                }, dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                });
    }

    private void finishWithTransition() {
        this.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right);
    }
}
