package com.ashwinrao.boxray.view;


import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentAddEditBinding;
import com.ashwinrao.boxray.util.Utilities;
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
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddEditFragment extends Fragment {

//    private Box mExistingBox;
//    private Box mNewBox;
    private LiveData<Box> mBoxLiveData;
    private ItemAdapter mAdapter;
    private List<String> mItems;
    private BoxViewModel mBoxViewModel;
    private FragmentManager mFragmentManager;
    private SharedPreferences mPreferences;
    private MutableLiveData<List<String>> mItemsMLD;

    private static final String CURRENT_BOX_IDENTIFIER = "current_box_identifier";

    private static final String TAG = "AddEditFragment";


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

//        Log.d(TAG, "Number of attached fragments (pre-removal): " + getActivity().getSupportFragmentManager().getFragments().size());

//        (getActivity()).getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getActivity().getSupportFragmentManager().findFragmentByTag("ListFragment"))).commit();

//        for (Fragment fragment : mFragmentManager.getFragments()) {
//            Log.d(TAG, fragment.getTag());
//        }

//        Log.d(TAG, "Number of attached fragments (post-removal): " + getActivity().getSupportFragmentManager().getFragments().size());


//        Log.d(TAG, "onCreate: " + mFragmentManager.getFragments());

//        mPreferences = getActivity().getSharedPreferences(getString(R.string.ID_LEDGER), Context.MODE_PRIVATE);

//        mFragmentManager.beginTransaction().addToBackStack(null).detach(Objects.requireNonNull(mFragmentManager.findFragmentByTag("ListFragment"))).commit();
//
//        if(mFragmentManager.findFragmentByTag("ListFragment").isDetached()) {
//            Log.d(TAG, "onCreate: ListFragment has been detached");
//        }

//        mOriginalSoftInputMode = Utilities.applyFragmentSoftInput(getActivity(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.add_edit_fragment_action_bar_title));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false);

        configureInputFields(binding);
        configureAddItemField(binding);
        configureChoosePhotoButton(binding);
        configureSoftInputBackgroundViewBehavior(binding);
        configureSaveBoxButton(binding);

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

        Objects.requireNonNull(binding.boxNumberInput.getEditText()).setText("43"); // todo replace dummy data

        return binding.getRoot();
    }

    private int getBoxNumFromSharedPref() {
        return mPreferences.getInt(CURRENT_BOX_IDENTIFIER, 1);  // returns 1 if key doesn't yet exist
    }

    private void saveBoxNumToSharedPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_BOX_IDENTIFIER, getBoxNumFromSharedPref() + 1);
        editor.apply();
    }

    private void saveBox(@NonNull final FragmentAddEditBinding binding) {
        Random r = new Random();    // todo replace with actual box id generated/retrieved from SharedPreferences
        if(checkBoxRequirements(binding.nameInput)) {

            Box box = new Box(r.nextInt(100),
                    binding.nameEditable.getText() == null ? "" : binding.nameEditable.getText().toString(),
                    binding.srcEditable.getText() == null ? "" : binding.srcEditable.getText().toString(),
                    binding.destEditable.getText() == null ? "" : binding.destEditable.getText().toString());
            mBoxViewModel.save(box);
            mFragmentManager.popBackStack();
        }
    }

//    private void saveBox(@NonNull final FragmentAddEditBinding binding) {
//        if(checkBoxRequirements(binding.nameInput)) {
//            // todo replace hard-coded box number with one stored/retrieved from SharedPreferences
//            Box box = new Box(43, binding.nameEditable.getText().toString(), binding.srcEditable.getText().toString(), binding.destEditable.getText().toString());
//            mBoxViewModel.save(box);
//            mFragmentManager.popBackStack();
//        }
//    }

    private boolean checkBoxRequirements(final TextInputLayout til) {
        boolean result = true;
        // Check name field for valid input; returns true if condition satisfied
        if(Objects.requireNonNull(til.getEditText()).getText().toString().isEmpty()) {
            til.setError("Make sure your box has a name");
            result = false;
        }

        til.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                til.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return result;
    }

    private void configureSaveBoxButton(@NonNull final FragmentAddEditBinding binding) {

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utilities.hideKeyboardFrom(Objects.requireNonNull(getActivity()), v);
                saveBox(binding);
            }
        });
    }

    private void configureSoftInputBackgroundViewBehavior(@NonNull final FragmentAddEditBinding binding) {
        binding.addEditRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideBackgroundViewOnSoftInputVisible(binding.addEditRoot, binding.bottomGraphicMars);
            }
        });
    }

    private void hideBackgroundViewOnSoftInputVisible(@NonNull ViewGroup root, @NonNull View viewToHide) {
        // Hides the specified view when the soft keyboard is visible, so as not to resize along with other views
        // Solution borrowed from: https://stackoverflow.com/questions/4745988/how-do-i-detect-if-software-keyboard-is-visible-on-android-device

        // todo use the implementation from Utilities.class (once it gets pulled out of ListFragment's onResume())
        Rect r = new Rect();
        root.getWindowVisibleDisplayFrame(r);
        int screenHeight = root.getRootView().getHeight();
        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        int keypadHeight = screenHeight - r.bottom;
        // applying 0.15 multiplier as threshold for min screenHeight that a keyboard should take up
        if (keypadHeight > screenHeight * 0.15) { viewToHide.setVisibility(View.INVISIBLE); } // keyboard is opened
        else { viewToHide.setVisibility(View.VISIBLE); } // keyboard is closed
    }

    private void toggleViewsOnChanged(@NonNull List<String> strings, final FragmentAddEditBinding binding) {
        if(strings.size() > 0) {
            binding.bottomGraphicMars.setImageDrawable(getResources().getDrawable(R.drawable.mars_fogg_frosted, Objects.requireNonNull(getActivity()).getTheme()));
        } else {
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
        mItems.add(item);
        mItemsMLD.setValue(mItems);
    }

    private void configureAddItemField(@NonNull final FragmentAddEditBinding binding) {

        binding.itemEditable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.itemPlusIcon.setImageResource(R.drawable.ic_add_item_enabled);
                    binding.itemContainer.setBackground(getResources().getDrawable(R.drawable.background_outline_add_item_field, Objects.requireNonNull(getActivity()).getTheme()));
                } else {
                    binding.itemPlusIcon.setImageResource(R.drawable.ic_add_item_disabled);
                    binding.itemContainer.setBackground(getResources().getDrawable(R.drawable.background_outline_photo_button, Objects.requireNonNull(getActivity()).getTheme()));
                }
            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.box_list_fragment_action_bar_title));
        ((MainActivity) getActivity()).getFloatingActionButton().setVisibility(View.VISIBLE);

        //        Utilities.applyFragmentSoftInput(getActivity(), mOriginalSoftInputMode);
    }
}
