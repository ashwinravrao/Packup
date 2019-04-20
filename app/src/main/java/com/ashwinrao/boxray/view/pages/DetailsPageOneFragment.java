package com.ashwinrao.boxray.view.pages;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentDetailsPageOneBinding;
import com.ashwinrao.boxray.view.MainActivity;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DetailsPageOneFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private static final String TAG = "Boxray";

    private boolean nameErrorSet;
    private BoxViewModel viewModel;
    private Resources.Theme appTheme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nameErrorSet = false;
        viewModel = ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel();
        appTheme = getActivity().getTheme();
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

        // Apply text appearance for floating hint (must be done at runtime to override default impl)
        final TextInputLayout[] tils = {binding.nameField, binding.sourceField, binding.destinationField, binding.notesField};
        for (TextInputLayout til : tils) {
            til.setHintTextAppearance(R.style.AppTheme_HintText);
            til.setBoxStrokeColor(getResources().getColor(R.color.colorAccent, appTheme));
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
                            viewModel.getCurrentBox().setName(s.toString());
                        } else {
                            setFieldError(field, getString(R.string.message_error_name_field_reprompt));
                            viewModel.getCurrentBox().setName(null);
                        }
                        break;
                    case "source":
                        viewModel.getCurrentBox().setSource(s.toString());
                        break;
                    case "destination":
                        viewModel.getCurrentBox().setDestination(s.toString());
                        break;
                    case "note":
                        viewModel.getCurrentBox().setNotes(s.toString());
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
        til.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, appTheme)));
        this.nameErrorSet = true;
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
