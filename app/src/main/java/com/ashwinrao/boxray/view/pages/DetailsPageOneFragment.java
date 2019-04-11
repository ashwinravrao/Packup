package com.ashwinrao.boxray.view.pages;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentDetailsPageOneBinding;
import com.ashwinrao.boxray.view.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DetailsPageOneFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private boolean nameErrorSet = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailsPageOneBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_page_one, container, false);

        configureToolbar(binding.toolbar);
        configureNameFieldStartBehavior(binding.nameField);
        watchNameField(binding.nameField);

        // Apply text appearance for floating hint (must be done at runtime to override default impl)
        final TextInputLayout[] tils = { binding.nameField, binding.sourceField, binding.destinationField, binding.notesField};
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

    private void configureNameFieldStartBehavior(@NonNull TextInputLayout nameField) {

        nameField.setErrorEnabled(true);
        nameField.setError(getString(R.string.label_required));
        nameField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, Objects.requireNonNull(getActivity()).getTheme())));
        nameErrorSet = true;
    }

    private void watchNameField(final TextInputLayout nameField) {
        Objects.requireNonNull(nameField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    if(nameErrorSet) { nameField.setErrorEnabled(false); }
                    ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().getBox().setName(s.toString());
                } else {
                    nameField.setErrorEnabled(true);
                    nameField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, Objects.requireNonNull(getActivity()).getTheme())));
                    nameErrorSet = true;
                    nameField.setError("Your box needs a name");
                    ((MainActivity) getActivity()).getViewModel().getBox().setName(null);
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.toolbar_save_draft:
                Snackbar.make(Objects.requireNonNull(getView()), R.string.message_draft_saved, Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Box box = ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().getBox();
//        if(box != null) {
//            if(box.getName() != null) {
//                Objects.requireNonNull(binding.nameField.getEditText()).setText(box.getName());
//            }
//        }
//    }
}
