package com.ashwinrao.locrate.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.view.activity.MainActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DestinationDialog extends AlertDialog implements Callback {

    private Context context;
    private View contentView;
    private GoogleMap map;

    private static final String TAG = "DestinationDialog";

    @SuppressLint("InflateParams")  // no parent (root) view to embed in
    public DestinationDialog(Context context, int overrideThemeResId) {
        super(context, overrideThemeResId);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_load_truck, null);
        setView(contentView);

        final MapView mapView = contentView.findViewById(R.id.map_view);
        final EditText addressField = contentView.findViewById(R.id.search_edit_text);
        final ImageView clearButton = contentView.findViewById(R.id.clear_address_button);

        initializeMap(context, mapView);
        initializeAddressField(addressField, clearButton, new OkHttpClient());
    }

    private void initializeMap(Context context, MapView mapView) {
        MapsInitializer.initialize(context);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync((GoogleMap googleMap) -> {
            map = googleMap;
            resetMap(googleMap, false);
        });
    }

    private void initializeAddressField(EditText field, ImageView button, OkHttpClient client) {
        button.setOnClickListener(v -> {
            field.setText("");
            hideSoftKeyboard();
            v.setVisibility(View.GONE);
        });

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) {
                    button.setVisibility(View.GONE);
                    resetMap(map, true);
                } else if (s.toString().length() > 0) {
                    button.setVisibility(View.VISIBLE);
                }
            }
        });

        field.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v.getText().toString().length() > 0) {
                    try {
                        Request request = new Request.Builder().url(getUrl(v.getText().toString(), context.getString(R.string.mapbox_access_token))).build();
                        client.newCall(request).enqueue(this);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "Error building URL for API request to mapbox.places endpoint");
                    }
                } else {
                    Toast.makeText(context, "You need to enter an address first", Toast.LENGTH_SHORT).show();
                }
                hideSoftKeyboard();
                return true;
            }
            return false;
        });
    }

    private String getUrl(@NonNull String searchText, @NonNull String apiToken) throws NullPointerException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.mapbox.com/geocoding/v5/mapbox.places").newBuilder();
        urlBuilder.addPathSegment(searchText + ".json");
        urlBuilder.addQueryParameter("access_token", apiToken);
        return urlBuilder.build().toString();
    }

    private void hideSoftKeyboard() {
        if(contentView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
        }
    }

    private void resetMap(GoogleMap map, boolean showAnimation) {
        LatLng latLng = new LatLng(41.881832, -87.623177);  // center on US (Chicago)
        map.getUiSettings().setAllGesturesEnabled(false);
        map.clear();
        if(showAnimation) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 1);
            map.animateCamera(cameraUpdate);
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) { }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if(response.isSuccessful()) {
            try {
                final String responseData = response.body().string();
                JSONObject root = new JSONObject(responseData);
                JSONArray array = root.getJSONArray("features");
                JSONArray center = array.getJSONObject(0).getJSONArray("center");
                LatLng latLng = new LatLng(center.getDouble(1), center.getDouble(0));

                ((MainActivity) context).runOnUiThread(() -> {
                    map.addMarker(new MarkerOptions().position(latLng));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    map.animateCamera(update);
                });

            } catch (NullPointerException npe) {
                Log.e(TAG, "onResponse: Error obtaining response body");
            } catch(JSONException jsone) {
                Log.e(TAG, "onResponse: Error parsing JSON response");
            }
        }
    }
}
