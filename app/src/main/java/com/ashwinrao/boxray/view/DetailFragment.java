package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentDetailBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Boxray";

    private LiveData<Box> liveBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            liveBox = ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel().getBoxByID(getArguments().getInt("ID"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        liveBox.observe(this, new Observer<Box>() {
            @Override
            public void onChanged(Box box) {
                binding.setBox(box);
                binding.boxNumber.setText(getString(R.string.title_detail_box_number, box.getId()));
                if(box.getNotes() == null) binding.noteContainer.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
