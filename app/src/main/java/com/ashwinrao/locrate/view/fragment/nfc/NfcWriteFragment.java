package com.ashwinrao.locrate.view.fragment.nfc;

import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.databinding.FragmentNfcWriteBinding;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.activity.NfcActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public class NfcWriteFragment extends Fragment implements NfcFragment, BackNavCallback {

    private String uuidToRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NfcActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);

        final Bundle extras = getActivity().getIntent().getExtras();

        if(extras != null) {
            String value = extras.getString("uuidToRegister");
            if (value != null) {
                uuidToRegister = value;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentNfcWriteBinding binding = FragmentNfcWriteBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onTagDetected(@NonNull Ndef ndef) {
            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", uuidToRegister.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                Objects.requireNonNull(getActivity()).finish();
            } catch (IOException | FormatException e) {
                e.printStackTrace();

            }
    }

    @Override
    public boolean onBackPressed() {
        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
        return true;
    }
}
