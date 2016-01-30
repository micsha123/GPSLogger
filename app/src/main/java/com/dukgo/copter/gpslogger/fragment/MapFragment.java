package com.dukgo.copter.gpslogger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dukgo.copter.gpslogger.R;
import com.dukgo.copter.gpslogger.service.GPSTracker;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

public class MapFragment extends Fragment {

    private MapView mapView;
    private GPSTracker gps;
    private Marker userMarker;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        Button findButton = (Button) rootView.findViewById(R.id.btn_find);
        Button saveButton = (Button) rootView.findViewById(R.id.btn_save);
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        gps = new GPSTracker(getActivity());

        mapView.setCenter(new LatLng(56.477, 84.954));
        mapView.setZoom(13);

//        mapView.setCenter(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()));

        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onShowMarker(MapView pMapView, Marker pMarker) {
            }

            @Override
            public void onHideMarker(MapView pMapView, Marker pMarker) {
            }

            @Override
            public void onTapMarker(MapView pMapView, Marker pMarker) {
            }

            @Override
            public void onLongPressMarker(MapView pMapView, Marker pMarker) {
            }

            @Override
            public void onTapMap(MapView pMapView, ILatLng pPosition) {
                if (userMarker != null) {
                    mapView.removeMarker(userMarker);
                }
                userMarker = new Marker(mapView, null, null, new LatLng(pPosition.getLatitude(), pPosition.getLongitude()));
                userMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, null, "42A5F5"));
                mapView.addMarker(userMarker);
            }

            @Override
            public void onLongPressMap(MapView pMapView, ILatLng pPosition) {

            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setCenter(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if(userMarker != null){
                    returnIntent.putExtra("location", userMarker.getPosition().toString());
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                }
                getActivity().finish();
            }
        });

        return rootView;
    }

    private Location getLocation(){
        if(gps.canGetLocation()){
            return gps.getLocation();
        }else{
            gps.showSettingsAlert();
            return null;
        }
    }
}
