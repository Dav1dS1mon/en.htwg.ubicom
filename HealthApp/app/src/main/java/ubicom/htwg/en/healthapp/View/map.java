package ubicom.htwg.en.healthapp.View;

import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.R;

//TODO: Change Map Activity - Merge with Bioharness3
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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        TextView textview;
        List<Location> coordinates = Recorder.coordinates;

        for(int i=0; i<coordinates.size(); i++) {
            Location coordinate = coordinates.get(i);
            LatLng currentPlace = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            System.err.println("DEBUG - MAP: Coordinates: Latiude: " + coordinate.getLatitude() + "|| Longitude: " + coordinate.getLongitude());

            if (i == 0) {
                if (coordinates.size() == 1)
                {
                    mMap.addMarker(new MarkerOptions().position(currentPlace).title("Start/Stop"));
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
