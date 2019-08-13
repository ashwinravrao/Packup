package com.ashwinrao.locrate.view.activity;

import android.content.Intent;
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
import com.ashwinrao.locrate.util.callback.SingleItemUnpackCallback;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.adapter.ItemDisplayAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class DetailActivity extends AppCompatActivity implements SingleItemUnpackCallback {

    private Box box;
    private RecyclerView recyclerView;
    private ItemDisplayAdapter adapter;
    private BoxViewModel boxViewModel;
    private ItemViewModel itemViewModel;
    private LiveData<Box> boxLD;
    private ActivityDetailBinding binding;
    private LiveData<List<Item>> itemsLD;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Locrate) getApplicationContext()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        boxViewModel = ViewModelProviders.of(this, factory).get(BoxViewModel.class);
        itemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);

        final Bundle extras = getIntent().getExtras();
        if(extras != null) {
            final String boxUUID = extras.getString("ID");
            if(boxUUID != null) {
                boxLD = boxViewModel.getBoxByUUID(boxUUID);
                itemsLD = itemViewModel.getItemsFromBox(boxUUID);
            } else {
                finishWithTransition();
            }
        } else {
            finishWithTransition();
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
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setupRecyclerView(@NonNull ActivityDetailBinding binding, @NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        addItemDecoration(this, recyclerView, 1);
        adapter = new ItemDisplayAdapter(this, true);
        adapter.setSingleItemUnpackCallback(this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        boxLD.observe(this, box -> {
            this.box = box;
            binding.setBox(box);
        });
        itemsLD.observe(this, items -> {
            adapter.submitList(items);
            adapter.setItemsForFiltering(items);
            binding.setNumItems(items.size());
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

    @Override
    public void unpackItem(@NonNull Item item, @NonNull Integer position) {
        itemViewModel.removeItemFromThis(item);
        adapter.submitList(itemViewModel.getItemsFromThis());
        binding.numberOfItems.setText(String.format(getString(R.string.number_of_items_format_string), itemViewModel.getItemsFromThis().size()));
        Snackbar.make(binding.snackbarContainer, "Item deleted", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                .setAction(R.string.undo, v -> {
                    itemViewModel.addItemToThis(item, position);
                    adapter.submitList(itemViewModel.getItemsFromThis());
                    binding.numberOfItems.setText(String.format(getString(R.string.number_of_items_format_string), itemViewModel.getItemsFromThis().size()));
                    recyclerView.setAdapter(adapter);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            itemViewModel.deleteItem(item);
                        }
                    }
                })
                .show();
    }
}
