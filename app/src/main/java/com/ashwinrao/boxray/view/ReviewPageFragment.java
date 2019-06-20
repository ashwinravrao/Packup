package com.ashwinrao.boxray.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentReviewPageBinding;
import com.ashwinrao.boxray.util.FilenameCreator;
import com.ashwinrao.boxray.util.PaginationCallback;
import com.ashwinrao.boxray.viewmodel.PhotoViewModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.boxray.util.KeyboardUtil.hideSoftKeyboard;

public class ReviewPageFragment extends Fragment {

    private int position = 0;
    private String path;
    private File file;
    private PhotoViewModel viewModel;
    private CircularProgressDrawable progress;

    private static final String TAG = "ReviewPageFragment";

    private PaginationCallback listener;

    @Inject
    ViewModelProvider.Factory factory;

    public void registerPaginationCallback(PaginationCallback listener) {
        this.listener = listener;
    }

    public void unregisterPaginationCallback() {
        this.listener = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(PhotoViewModel.class);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
            path = getArguments().getString("path");
            file = viewModel.getFileWithPath(path);
        }
        this.progress = new CircularProgressDrawable(Objects.requireNonNull(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentReviewPageBinding binding = FragmentReviewPageBinding.inflate(inflater);
        setupImageView(binding.imageView);
        setupNameField(binding.nameField);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterPaginationCallback();
    }

    private void setupImageView(@NonNull ImageView imageView) {
        setupProgressIndicator();
        Glide.with(Objects.requireNonNull(getContext())).load(file).placeholder(progress).into(imageView);
    }

    private void setupProgressIndicator() {
        this.progress.setTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
        this.progress.setStrokeWidth(5f);
        this.progress.setCenterRadius(30f);
        this.progress.start();
    }

    private void setupNameField(@NonNull EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                hideSoftKeyboard(Objects.requireNonNull(getContext()), editText.getRootView());
                if (v.getText().toString().length() > 0) {
                    if(renameFile(v.getText().toString())) {
                        Toast.makeText(getContext(), "Image renamed", Toast.LENGTH_SHORT).show();
                        if (listener != null) { listener.progress(); }
                    } else {
                        Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
            return false;
        });
    }

    private boolean renameFile(String newName) {
        File newFile = new File(Objects.requireNonNull(getActivity())
                .getExternalMediaDirs()[0] +
                FilenameCreator.create(newName));
        Log.d(TAG, "oldFile abs path: " + file.getAbsolutePath());
        Log.d(TAG, "newFile abs path: " + newFile.getAbsolutePath());
        viewModel.setPathAtPosition(position, newFile.getAbsolutePath());
        return file.renameTo(newFile);
    }
}
