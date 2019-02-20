package com.ashwinrao.boxray.view;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentAddEditBinding;
import com.ashwinrao.boxray.view.adapter.ItemAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddEditFragment extends Fragment {

//    private int mOriginalSoftInputMode;
//    private Box mExistingBox;
//    private Box mNewBox;
//    private int mBoxIdentifier;
    private LiveData<Box> mBoxLiveData;
    private List<String> mItems;
    private BoxViewModel mBoxViewModel;
    private FragmentManager mFragmentManager;
    private MutableLiveData<List<String>> mItemsMLD;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = ((MainActivity) Objects.requireNonNull(getActivity())).getFloatingActionButton();
        if(fab != null) { fab.setVisibility(View.GONE); }

        mFragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(getActivity().getApplication());
        mBoxViewModel = factory.create(BoxViewModel.class);
        mItems = new ArrayList<>();
        mItemsMLD = new MutableLiveData<>();

//        mOriginalSoftInputMode = Utilities.applyFragmentSoftInput(getActivity(), null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false);
        configureInputFields(binding);
        configureAddItemField(binding);
        configureChoosePhotoButton(binding);

        // Items RecyclerView
        binding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ItemAdapter adapter = new ItemAdapter(getActivity(), (Objects.requireNonNull(getActivity()))
                .getWindow()
                .getDecorView()
                .findViewById(R.id.drawer_layout), mItemsMLD);
        binding.itemRecyclerView.setAdapter(adapter);
        mItemsMLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if(strings.size() > 0) { binding.divider.setVisibility(View.VISIBLE); }
                else { binding.divider.setVisibility(View.INVISIBLE); }
                adapter.setAdapterItems(strings);
                binding.itemRecyclerView.setAdapter(adapter);
            }
        });

        return binding.getRoot();
    }

    private void configureChoosePhotoButton(@NonNull final FragmentAddEditBinding binding) {
        binding.choosePhotoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().findViewById(R.id.drawer_layout), "Add more items", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void saveItem(String item) {
        mItems.add(0, item);
        mItemsMLD.setValue(mItems);
    }

    private void configureAddItemField(@NonNull final FragmentAddEditBinding binding) {
        binding.addItemFieldEditable.setHint(R.string.add_item_field_title);
        binding.addItemFieldEditable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    binding.addItemImageView.setImageResource(R.drawable.ic_add_item_enabled);
                } else {
                    binding.addItemImageView.setImageResource(R.drawable.ic_add_item_disabled);
                }
            }
        });

        binding.addItemFieldEditable.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add item to recycler view on "Return" key press
        binding.addItemFieldEditable.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    if(v.getText().toString().length() >= 1) {
                        saveItem(binding.addItemFieldEditable.getText().toString());
                        v.setText(null);
                    } else {
                        Toast.makeText(getActivity(), "Make sure to name your item", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void configureInputFields(@NonNull final FragmentAddEditBinding binding) {
        // Bring nested scroll view to front (over the bottom-anchored ImageView)
        binding.nestedScrollView.bringToFront();

        String[] field_titles = {getString(R.string.name_field_title), getString(R.string.source_field_title), getString(R.string.dest_field_title)};
        final View[] views = {binding.nameInput, binding.sourceInput, binding.destInput};

        for (int i = 0; i < views.length; i++) {
            final TextView hint = views[i].findViewById(R.id.input_field_hint);
            final EditText editable = views[i].findViewById(R.id.input_field_editable);

            hint.setText(field_titles[i]);
            editable.setHint(field_titles[i]);

            editable.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().length() > 0) {
                        hint.setVisibility(View.VISIBLE);
                    } else {
                        hint.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        Utilities.applyFragmentSoftInput(getActivity(), mOriginalSoftInputMode);
//    }
}
