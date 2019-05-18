package com.ashwinrao.boxray.view.pages;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentDetailsPageOneBinding;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

public class DetailsPageOneFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private static final String TAG = "Boxray";
    private boolean nameErrorSet;
    private BoxViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nameErrorSet = false;
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailsPageOneBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_page_one, container, false);

        final TextInputLayout[] fields = {binding.nameField, binding.sourceField, binding.destinationField, binding.notesField};

        configureToolbar(binding.toolbar);
        setFieldError(binding.nameField, getString(R.string.label_required));
        for(TextInputLayout field : fields) {
            watchField(field);
        }

        if(savedInstanceState != null) {
            repopulateFields(fields);
        }

        // Apply text appearance for floating hint (must be done at runtime to override default impl)
        final TextInputLayout[] tils = {binding.nameField, binding.sourceField, binding.destinationField, binding.notesField};
        for (TextInputLayout til : tils) {
            til.setHintTextAppearance(R.style.AppTheme_HintText);
            til.setBoxStrokeColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));
        }

        return binding.getRoot();
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(getString(R.string.title_page_one_details));
        toolbar.inflateMenu(R.menu.menu_toolbar_page_one);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void watchField(final TextInputLayout field) {
        Objects.requireNonNull(field.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch(field.getTag().toString()) {
                    case "name":
                        if(s.toString().length() > 0) {
                            if (nameErrorSet) field.setErrorEnabled(false);
                            viewModel.getBox().setName(s.toString()); } else {
                            setFieldError(field, getString(R.string.message_error_name_field_reprompt));
                            viewModel.getBox().setName(null);
                        }
                        break;
                    case "source":
                        viewModel.getBox().setSource(s.toString());
                        break;
                    case "destination":
                        viewModel.getBox().setDestination(s.toString());
                        break;
                    case "note":
                        viewModel.getBox().setNotes(s.toString());
                        break;
                    default:
                        Log.e(TAG, "DetailsPageOneFragment: watchField(): TextWatcher: afterTextChanged: error retrieving TextInputLayout tag");
                }
            }
        });
    }

    private void setFieldError(@NonNull TextInputLayout til, String message) {
        til.setErrorEnabled(true);
        til.setError(message);
        til.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, Objects.requireNonNull(getActivity()).getTheme())));
        this.nameErrorSet = true;
    }

    private void repopulateFields(@NonNull final TextInputLayout[] fields) {
        if(viewModel.getBox().getName() != null) {
            nameErrorSet = false;
            fields[0].setErrorEnabled(false);
        }
        Objects.requireNonNull(fields[0].getEditText()).setText(viewModel.getBox().getName());
        Objects.requireNonNull(fields[1].getEditText()).setText(viewModel.getBox().getSource());
        Objects.requireNonNull(fields[2].getEditText()).setText(viewModel.getBox().getDestination());
        Objects.requireNonNull(fields[3].getEditText()).setText(viewModel.getBox().getNotes());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save_draft:
                Snackbar.make(Objects.requireNonNull(getView()), R.string.message_draft_saved, Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
