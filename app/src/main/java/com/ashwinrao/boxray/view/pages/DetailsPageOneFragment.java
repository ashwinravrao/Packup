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

        // Apply "hint" text appearance for all input fields @ runtime (mainly to override floating label color inheritance from base theme)
        final TextInputLayout[] tils = { binding.nameField, binding.sourceField, binding.destinationField, binding.notesField};
        for (TextInputLayout til : tils) {
            til.setHintTextAppearance(R.style.AppTheme_HintText);
            til.setBoxStrokeColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));
        }

//        // Set "completed" icon when required fields are satisfied
//        ((MainActivity) getActivity()).getViewModel().getCanViewPagerAdvance().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean) {
//                    binding.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_completed_tag, Objects.requireNonNull(getActivity()).getTheme()));
//                } else {
//                    binding.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_tag, Objects.requireNonNull(getActivity()).getTheme()));
//                }
//            }
//        });

        return binding.getRoot();
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(getString(R.string.title_page_one_details));
        toolbar.inflateMenu(R.menu.menu_toolbar_page_one);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void configureNameFieldStartBehavior(@NonNull TextInputLayout nameField) {

        // Start off with error indicating name field required (avoids intercepting + handling swipe events)
        nameField.setErrorEnabled(true);
        nameField.setError(getString(R.string.label_required));
        nameField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, Objects.requireNonNull(getActivity()).getTheme())));
        nameErrorSet = true;
//        ((MainActivity) getActivity()).getViewModel().setCanViewPagerAdvance(false);
    }

    private void watchNameField(final TextInputLayout nameField) {
        Objects.requireNonNull(nameField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    if(nameErrorSet) { nameField.setErrorEnabled(false); }
//                    ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().setCanViewPagerAdvance(true);
                } else {
                    nameField.setErrorEnabled(true);
                    nameField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.warningRed, Objects.requireNonNull(getActivity()).getTheme())));
                    nameErrorSet = true;
//                    ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().setCanViewPagerAdvance(false);
                    nameField.setError("Your box needs a name");
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
//            case R.id.toolbar_add_note:
////                binding.notesField.setVisibility(View.VISIBLE);
//                ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.message_add_note, true, true);
//                return true;
            case R.id.toolbar_save_draft:
//                ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.message_draft_saved, true, true);
                Snackbar.make(Objects.requireNonNull(getView()), R.string.message_draft_saved, Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
