package com.ashwinrao.boxray.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ashwinrao.boxray.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LoadTruckDialog {

    private View dialogView;

    public void build(@NonNull final Context context,
                             @Nullable final Integer customViewLayoutId,
                             int positiveButtonTitleString,
                             int negativeButtonTitleString,
                             @Nullable DialogInterface.OnClickListener positiveButtonClickListener,
                             @Nullable DialogInterface.OnClickListener negativeButtonClickListener) {

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

        MapView mapView = dialogView.findViewById(R.id.map_view);
        MapsInitializer.initialize(context);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(37.773972, -122.431297); // todo: remove hardcoded location
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Old Home"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        dialog.show();

        Button[] buttons = {dialog.getButton(AlertDialog.BUTTON_POSITIVE), dialog.getButton(AlertDialog.BUTTON_NEGATIVE)};
        for (Button button : buttons) {
            button.setTextColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }
    }
}
