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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class AddEditFragment extends Fragment {

//    private int mOriginalSoftInputMode;
//    private Box mExistingBox;
//    private Box mNewBox;
//    private int mBoxIdentifier;
    private LiveData<Box> mBoxLiveData;
    private ItemAdapter mAdapter;
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
        mAdapter = new ItemAdapter(getActivity(), (Objects.requireNonNull(getActivity()))
                .getWindow()
                .getDecorView()
                .findViewById(R.id.drawer_layout), mItemsMLD);
        binding.itemRecyclerView.setAdapter(mAdapter);

        mItemsMLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                toggleViewsOnChanged(strings, binding);
                mAdapter.setAdapterItems(strings);
                binding.itemRecyclerView.setAdapter(mAdapter);
            }
        });

        return binding.getRoot();
    }

    private void toggleViewsOnChanged(@NonNull List<String> strings, final FragmentAddEditBinding binding) {
        if(strings.size() > 0) {
            binding.divider.setVisibility(View.VISIBLE);
            binding.bottomGraphicMars.setImageDrawable(getResources().getDrawable(R.drawable.mars_fogg_frosted, Objects.requireNonNull(getActivity()).getTheme()));
        } else {
            binding.divider.setVisibility(View.INVISIBLE);
            binding.bottomGraphicMars.setImageDrawable(getResources().getDrawable(R.drawable.mars_fogg_edited, Objects.requireNonNull(getActivity()).getTheme()));
        }

    }

    private void configureChoosePhotoButton(@NonNull final FragmentAddEditBinding binding) {
        binding.photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItems.size() == 0) {
                    Snackbar.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().findViewById(R.id.drawer_layout), "Add more items", Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Opening image picker...", Toast.LENGTH_SHORT).show();    // todo replace with image chooser dialog
                }
            }
        });
    }

    private void saveItem(String item) {
        mItems.add(0, item);
        mItemsMLD.setValue(mItems);
    }

    private void configureAddItemField(@NonNull final FragmentAddEditBinding binding) {

        binding.itemEditable.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add item to recycler view on "Return" key press
        binding.itemEditable.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    if(v.getText().toString().length() >= 1) {
                        saveItem(Objects.requireNonNull(binding.itemEditable.getText()).toString());
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

        final TextInputEditText[] tiets = {binding.nameEditable, binding.srcEditable, binding.destEditable};

        for (final TextInputEditText tiet : tiets) {
            tiet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        tiet.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorAccent));
                    } else {
                        tiet.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.textcolor));
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
