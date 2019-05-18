package com.ashwinrao.boxray.view.pages;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentContentsPageTwoBinding;
import com.ashwinrao.boxray.view.adapter.ItemAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

public class ContentsPageTwoFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private View viewForSnackbar;
    private List<String> items;
    private MutableLiveData<List<String>> itemsMLD;
    private ItemAdapter itemAdapter;
    private BoxViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        itemsMLD = new MutableLiveData<>();
        viewModel = ViewModelProviders.of(getActivity(), factory).get(BoxViewModel.class);
        viewForSnackbar = getActivity().getWindow().getDecorView().findViewById(R.id.fragment_container);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentContentsPageTwoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contents_page_two, container, false);

        configureToolbar(binding.toolbar);
        configureItemRecyclerView(binding.itemRecyclerView, savedInstanceState);
        configureItemField(binding.itemField);
        return binding.getRoot();
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(getString(R.string.title_page_two_items));
        toolbar.inflateMenu(R.menu.menu_toolbar_page_two);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void configureItemRecyclerView(@NonNull final RecyclerView rv, @Nullable Bundle savedInstanceState) {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemAdapter = new ItemAdapter(Objects.requireNonNull(getActivity()), viewForSnackbar, itemsMLD, items, true);
        rv.setAdapter(itemAdapter);

        itemsMLD.observe(this, strings -> {
            itemAdapter.setItems(strings);
            rv.setAdapter(itemAdapter);
            viewModel.getBox().setContents(strings);
        });

        if(savedInstanceState != null) {
            items = viewModel.getBox().getContents();
            itemsMLD.setValue(items);
            itemAdapter.setItems(items);
            rv.setAdapter(itemAdapter);
        }
    }

    private void configureItemField(@NonNull final TextInputLayout itemField) {

        itemField.setHintTextAppearance(R.style.AppTheme_HintText);
        itemField.setBoxStrokeColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));

        Objects.requireNonNull(itemField.getEditText()).setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add item to recycler view on "Return" key press
        itemField.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v.getText().toString().length() >= 1) {
                    saveItem(Objects.requireNonNull(itemField.getEditText().getText()).toString());
                    v.setText(null);
                } else {
                    itemField.setErrorEnabled(true);
                    itemField.setError("Make sure to name your item");
                    itemField.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            itemField.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
                return true;
            }
            return false;
        });

    }

    private void saveItem(String item) {
        items.add(0, item);
        itemsMLD.setValue(items);
    }

    private void clearItems() {
        final List<String> localItems = new ArrayList<>(items);
        items.clear();
        itemsMLD.setValue(items);
        itemAdapter.setItems(items);
        Snackbar.make(viewForSnackbar,
                R.string.message_items_cleared,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.message_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        items = localItems;
                        itemsMLD.setValue(items);
                        itemAdapter.setItems(items);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()))
                .show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_clear_all:
                clearItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
