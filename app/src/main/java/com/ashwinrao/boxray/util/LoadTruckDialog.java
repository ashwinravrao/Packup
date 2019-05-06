package com.ashwinrao.boxray.util;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.view.MainActivity;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadTruckDialog implements Callback {

    private Context context;
    private View dialogView;
    private GoogleMap googleMap;
    private static final String TAG = "LoadTruckDialog";

    public void build(@NonNull final Context context,
                             @Nullable final Integer customViewLayoutId,
                             int positiveButtonTitleString,
                             int negativeButtonTitleString,
                             @Nullable DialogInterface.OnClickListener positiveButtonClickListener,
                             @Nullable DialogInterface.OnClickListener negativeButtonClickListener) {

        this.context = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_DialogButtons);

        if(customViewLayoutId != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            dialogView = inflater.inflate(customViewLayoutId, null);
            builder.setView(dialogView);
        }

        if(positiveButtonClickListener != null) {
            builder.setPositiveButton(context.getString(positiveButtonTitleString), positiveButtonClickListener);
        }

        if(negativeButtonClickListener != null) {
            builder.setNegativeButton(context.getString(negativeButtonTitleString), negativeButtonClickListener);
        }

        AlertDialog dialog = builder.create();

        final EditText search = dialogView.findViewById(R.id.search_edit_text);
        final ImageView clearAddressButton = dialogView.findViewById(R.id.clear_address_button);
        final MapView mapView = dialogView.findViewById(R.id.map_view);

        MapsInitializer.initialize(context);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();

        mapView.getMapAsync((GoogleMap googleMap) -> {
            this.googleMap = googleMap;
            resetMap(googleMap, false);
        });

        clearAddressButton.setOnClickListener(v -> {
            search.setText("");
            hideSoftKeyboard();
            v.setVisibility(View.GONE);
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) {
                    clearAddressButton.setVisibility(View.GONE);
                    resetMap(googleMap, true);
                } else if (s.toString().length() > 0) {
                    clearAddressButton.setVisibility(View.VISIBLE);
                }
            }
        });

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v.getText().toString().length() > 0) {
                    try {
                        OkHttpClient client = new OkHttpClient();
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

        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button[] buttons = {dialog.getButton(AlertDialog.BUTTON_POSITIVE), dialog.getButton(AlertDialog.BUTTON_NEGATIVE)};
        for (Button button : buttons) {
            button.setTextColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }
    }

    private void hideSoftKeyboard() {
        if(dialogView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
        }
    }

    private void resetMap(@NonNull GoogleMap googleMap, boolean animation) {
        LatLng latLng = new LatLng(41.881832, -87.623177);  // centered on US
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.clear();
        if(animation) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 1);
            googleMap.animateCamera(cameraUpdate);
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private String getUrl(@NonNull String searchText, @NonNull String apiToken) throws NullPointerException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.mapbox.com/geocoding/v5/mapbox.places").newBuilder();
        urlBuilder.addPathSegment(searchText + ".json");
        urlBuilder.addQueryParameter("access_token", apiToken);
        return urlBuilder.build().toString();
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
                    googleMap.addMarker(new MarkerOptions().position(latLng));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    googleMap.animateCamera(update);
                });

            } catch (NullPointerException npe) {
                Log.e(TAG, "onResponse: Error obtaining response body");
            } catch(JSONException jsone) {
                Log.e(TAG, "onResponse: Error parsing JSON response");
            }
        }
    }
}
