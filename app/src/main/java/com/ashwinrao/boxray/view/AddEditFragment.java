package com.ashwinrao.boxray.view;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddEditFragment extends Fragment {

    private Toolbar mToolbar;
    private List<String> mItems;
    private int mDummyBoxNumber;    // todo remove when box number is retrieved from SharedPref
    private ItemAdapter mAdapter;
    private BoxViewModel mBoxViewModel;
    private SharedPreferences mPreferences;
    private MutableLiveData<List<String>> mItemsMLD;

    private static final String TAG = AddEditFragment.class.getName();
    private static final String CURRENT_BOX_IDENTIFIER = "current_box_identifier";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Obtain ViewModel reference
        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(Objects.requireNonNull(getActivity()).getApplication());
        mBoxViewModel = factory.create(BoxViewModel.class);

        // Instantiate global vars
        mItems = new ArrayList<>();
        mItemsMLD = new MutableLiveData<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false);

        configureInputFields(binding);

        configureAddItemField(binding);

        configureChoosePhotoButton(binding);

//        configureSoftInputBackgroundViewBehavior(binding);

//        configureSaveBoxButton(binding);

        configureLabelInstructionsButton(binding);

        configureItemRecyclerView(binding);

        generateDummyBoxNumber(); // todo replace dummy data

        return binding.getRoot();
    }

    private void configureToolbar() {
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

//        Menu menu = ((MainActivity)getActivity()).getMenu();
//        getActivity().getMenuInflater().inflate(R.menu.add_edit, menu);
    }

    // View configuration methods
    private void configureInputFields(@NonNull final FragmentAddEditBinding binding) {
        // Bring nested scroll view to front (over the bottom-anchored ImageView)
//        binding.nestedScrollView.bringToFront();

        final TextInputEditText[] tiets = {binding.nameEditable, binding.sourceEditable, binding.destEditable, binding.addItemEditable};

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

    private void configureAddItemField(@NonNull final FragmentAddEditBinding binding) {

//        binding.itemEditable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus) {
//                    binding.itemPlusIcon.setImageResource(R.drawable.ic_add_item_enabled);
//                    binding.itemContainer.setBackground(getResources().getDrawable(R.drawable.background_outline_add_item_field, Objects.requireNonNull(getActivity()).getTheme()));
//                } else {
//                    binding.itemPlusIcon.setImageResource(R.drawable.ic_add_item_disabled);
//                    binding.itemContainer.setBackground(getResources().getDrawable(R.drawable.background_outline_photo_button, Objects.requireNonNull(getActivity()).getTheme()));
//                }
//            }
//        });

        binding.addItemEditable.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Add item to recycler view on "Return" key press
        binding.addItemEditable.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(v.getText().toString().length() >= 1) {
                        saveItem(Objects.requireNonNull(binding.addItemEditable.getText()).toString());
                        v.setText(null);
                    } else {
                        binding.addItemField.setError("Make sure to name your item");
                        binding.addItemEditable.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                binding.addItemField.setErrorEnabled(false);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void configureChoosePhotoButton(@NonNull final FragmentAddEditBinding binding) {
        binding.photoPlaceholder.setOnClickListener(new View.OnClickListener() {
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

//    private void configureSoftInputBackgroundViewBehavior(@NonNull final FragmentAddEditBinding binding) {
//        binding.addEditRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                hideBackgroundViewOnSoftInputVisible(binding.addEditRoot, binding.bottomGraphicMars);
//            }
//        });
//    }

//    private void configureSaveBoxButton(@NonNull final FragmentAddEditBinding binding) {
//        binding.saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveBox(binding);
//            }
//        });
//    }

    private void configureLabelInstructionsButton(@NonNull final FragmentAddEditBinding binding) {
        binding.labelPlacementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLabeInstructionsDialog(Objects.requireNonNull(getActivity()));
            }
        });
    }

    private void configureItemRecyclerView(@NonNull final FragmentAddEditBinding binding) {
        binding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ItemAdapter(getActivity(), (Objects.requireNonNull(getActivity()))
                .getWindow()
                .getDecorView()
                .findViewById(R.id.drawer_layout), mItemsMLD);
        binding.itemRecyclerView.setAdapter(mAdapter);

        mItemsMLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
//                toggleViewsOnChanged(strings, binding);
                mAdapter.setAdapterItems(strings);
                binding.itemRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    // View backend logic
    private void generateDummyBoxNumber() {
        Random r = new Random();
        mDummyBoxNumber = r.nextInt(100);
    }

    private int getBoxNumFromSharedPref() {
        return mPreferences.getInt(CURRENT_BOX_IDENTIFIER, 1);  // returns 1 if key doesn't yet exist
    }

    private void saveBoxNumToSharedPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_BOX_IDENTIFIER, getBoxNumFromSharedPref() + 1);
        editor.apply();
    }

//    private void saveBox(@NonNull final FragmentAddEditBinding binding) {
//        if(checkBoxRequirements(binding.nameField)) {
//            Box box = new Box(mDummyBoxNumber,  // todo replace with actual box number
//                    binding.nameEditable.getText() == null ? "" : binding.nameEditable.getText().toString(),
//                    binding.sourceEditable.getText() == null ? "" : binding.sourceEditable.getText().toString(),
//                    binding.destEditable.getText() == null ? "" : binding.destEditable.getText().toString(),
//                    mItems);
//            mBoxViewModel.save(box);
//            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
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

    private void hideBackgroundViewOnSoftInputVisible(@NonNull ViewGroup root, @NonNull View viewToHide) {

        // Hides the specified view when the soft keyboard is visible, so as not to resize along with other views
        if(Utilities.keyboardIsShowing(root)) { viewToHide.setVisibility(View.INVISIBLE); }
        else { viewToHide.setVisibility(View.VISIBLE); }
    }

//    private void toggleViewsOnChanged(@NonNull List<String> strings, final FragmentAddEditBinding binding) {
//        if(strings.size() > 0) {
//            binding.bottomGraphicMars.setImageDrawable(getResources().getDrawable(R.drawable.mars_fogg_frosted, Objects.requireNonNull(getActivity()).getTheme()));
//        } else {
//            binding.bottomGraphicMars.setImageDrawable(getResources().getDrawable(R.drawable.mars_fogg_edited, Objects.requireNonNull(getActivity()).getTheme()));
//        }
//
//    }

    private void saveItem(String item) {
        mItems.add(item);
        mItemsMLD.setValue(mItems);
    }

    private void showLabeInstructionsDialog(@NonNull final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog_LabelInstructions);
        builder.setTitle(context.getString(R.string.label_instructions_dialog_title))
                .setMessage(context.getString(R.string.label_instructions_dialog_body))
                .setPositiveButton(context.getString(R.string.label_instructions_dialog_positive_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(getResources().getDrawable(R.drawable.background_label_instructions_dialog, Objects.requireNonNull(getActivity()).getTheme()));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.add_edit_fragment_action_bar_title));
        configureToolbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
