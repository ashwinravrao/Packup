package com.ashwinrao.boxray.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentAddEditBinding;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class AddEditFragment extends Fragment {

    private int mOriginalSoftInputMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOriginalSoftInputMode = applyFragmentSoftInput(getActivity(), null);
    }

    /**
     * @param activity
     * @param originalSoftInputMode
     *
     * Saves current window softInputMode (from AndroidManifest),
     * and changes to WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING.
     *
     * In onDestroy() this method is called again to restore the original config to the Manifest.
     *
     * @return manifest WindowManager.Attributes.SoftInputMode from before this Fragment was inflated.
     */

    private int applyFragmentSoftInput(FragmentActivity activity, @Nullable Integer originalSoftInputMode) {
        FragmentActivity act = Objects.requireNonNull(activity);
        if(originalSoftInputMode != null) {
            activity.getWindow().setSoftInputMode(originalSoftInputMode);
            return -1;
        } else {
            int original = act.getWindow().getAttributes().softInputMode;
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            return original;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAddEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        applyFragmentSoftInput(getActivity(), mOriginalSoftInputMode);
    }
}
