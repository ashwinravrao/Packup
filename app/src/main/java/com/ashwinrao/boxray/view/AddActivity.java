package com.ashwinrao.boxray.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.util.BackNavCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private HashMap<Class, BackNavCallback> listeners = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new AddFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void registerBackNavigationListener(@NonNull Class name, @NonNull BackNavCallback listener) {
        listeners.put(name, listener);
    }

    public void unregisterBackNavigationListener(@NonNull Class name) {
        listeners.remove(name);
    }

    @Override
    public void onBackPressed() {
        // todo solve why this doesn't work
        boolean wasIntercepted = false;
        final Iterator iter = listeners.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            if(getSupportFragmentManager().getFragments().get(0).getClass() == pair.getKey()) {
                wasIntercepted = true;
                ((BackNavCallback) pair.getValue()).onBackPressed();
            }
            iter.remove();
        }
        if(!wasIntercepted) {
            super.onBackPressed();
        }
    }
}
