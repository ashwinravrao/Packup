package com.ashwinrao.locrate.view.fragment.nfc;

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
import androidx.lifecycle.LiveData;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentNfcReadBinding;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.activity.DetailActivity;
import com.ashwinrao.locrate.view.activity.NfcActivity;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


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
        return binding.getRoot();
    }

    @Override
    public void onTagDetected(@NonNull Ndef ndef) {
        try {
            ndef.connect();
            final NdefMessage ndefMessage = ndef.getNdefMessage();
            final UUID boxUUID = UUID.fromString(new String(ndefMessage.getRecords()[0].getPayload()));

            if(boxUUID != null) {
                final Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ID", boxUUID);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "This tag isn't registered to a box yet", Toast.LENGTH_SHORT).show();
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
