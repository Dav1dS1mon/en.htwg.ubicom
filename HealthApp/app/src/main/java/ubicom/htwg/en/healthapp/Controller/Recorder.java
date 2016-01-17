package ubicom.htwg.en.healthapp.Controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ubicom.htwg.en.healthapp.R;

public class Recorder extends Service implements LocationListener {
    public static List<Location> coordinates = new ArrayList<Location>();
    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        coordinates.clear();
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        coordinates.add(location);
        System.err.println("DEBUG - Location: Coordinates: Latiude: " + location.getLatitude() + "|| Longitude: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
