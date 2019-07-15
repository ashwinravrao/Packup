package com.ashwinrao.locrate.view.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.fragment.nfc.NfcReadFragment;
import com.ashwinrao.locrate.view.fragment.nfc.NfcWriteFragment;
import com.ashwinrao.locrate.view.fragment.nfc.NfcFragment;


public class NfcActivity extends AppCompatActivity {

    private NfcFragment fragment;
    private NfcAdapter nfcAdapter;
    private boolean isWrite;
    private BackNavCallback listener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        initializeNfcAdapter();
        showContextDependentFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter[] intentFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        };

        final PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void initializeNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showContextDependentFragment() {
        if (getIntent().getExtras() != null && fragment == null) {
            isWrite = getIntent().getExtras().getBoolean("isWrite");
            fragment = isWrite ? new NfcWriteFragment() : new NfcReadFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        (Fragment) fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            final Ndef ndef = Ndef.get(tag);
            fragment.onTagDetected(ndef);
        }
    }

    public void registerBackNavigationListener(@NonNull BackNavCallback listener) {
        this.listener = listener;
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            if(!listener.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

}
