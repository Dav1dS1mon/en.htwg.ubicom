/*
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ubicom.htwg.en.healthapp.View;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

import ubicom.htwg.en.healthapp.Controller.BioHarnessBT.MainActivity;
import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.Model.menu;
import ubicom.htwg.en.healthapp.R;

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button okButton = (Button) findViewById(R.id.button_closeDiagramm);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                /*Functionality to act if the button DISCONNECT is touched*/
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        long diffTime = menu.getDiffTime() / 1000;
        List<Location> coordinates = Recorder.coordinates;
        List<Bundle> data = MainActivity.data;
        int avg_route = data.size() / coordinates.size();

        //TODO

        for(int i=0; i<coordinates.size(); i++) {
            Location coordinate = coordinates.get(i);
            LatLng currentPlace = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            if (i == 0) {
                if (coordinates.size() == 1)
                {
                    mMap.addMarker(new MarkerOptions().position(currentPlace).snippet(""));
                } else {
                    mMap.addMarker(new MarkerOptions().position(currentPlace).title("Start"));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPlace.latitude, currentPlace.longitude), 20.0f));
            } else if (i == (coordinates.size() - 1)) {
                mMap.addMarker(new MarkerOptions().position(currentPlace).title("Stop"));
            } else {
                mMap.addMarker(new MarkerOptions().position(currentPlace));
            }
        }
    }
}
