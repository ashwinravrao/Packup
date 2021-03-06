package com.ashwinrao.packup.view.fragment.nfc;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashwinrao.packup.R;
import com.ashwinrao.packup.databinding.FragmentNfcReadBinding;
import com.ashwinrao.packup.util.callback.BackNavCallback;
import com.ashwinrao.packup.view.activity.DetailActivity;
import com.ashwinrao.packup.view.activity.NfcActivity;

import java.io.IOException;
import java.util.Objects;


public class NfcReadFragment extends Fragment implements NfcFragment, BackNavCallback {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NfcActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentNfcReadBinding binding = FragmentNfcReadBinding.inflate(inflater);
        binding.toolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        return binding.getRoot();
    }

    @Override
    public void onTagDetected(@NonNull Ndef ndef) {
        try {
            ndef.connect();
            final NdefMessage ndefMessage = ndef.getNdefMessage();
            final String boxUUID = new String(ndefMessage.getRecords()[0].getPayload());

            if(boxUUID.length() > 0) {
                final Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ID", boxUUID);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_from_right, R.anim.stay_still);
                getActivity().finishAfterTransition();
            } else {
                Toast.makeText(getActivity(), "This tag isn't registered", Toast.LENGTH_SHORT).show();
            }
            ndef.close();
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
