/*
 * @Author:         David Simon
 * @Semester:       WS2015/2016
 * @Professor:      Professor Doctor Seepold
 * @Description:    Connect with GPS Tracker from smartphone and create a route.
 * @Sources:        "Android 4 - Apps entwickeln mit dem Android SDK" von Thomas Künneth
 *                  - "Kapitel 9: Sensoren und GPS" - Page 227 - 248
 */
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

/*
 * @Description:    Service for the GPS Tracker
 */
public class Recorder extends Service implements LocationListener {
    //Local variable
    public static List<Location> coordinates = new ArrayList<Location>();
    private LocationManager locationManager;

    /*
     * @Description:    Override method from LocationListener
     * @Intent:         Intent-Variable
     * @Return:         Null
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /*
     * @Description: Init the GPS Tracker.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        coordinates.clear();
    }

    /*
     * @Description: Remove GPS Trakcer service
     */
    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    /*
     * @Description:    Update Location - If Location changed
     * @Location:       Current Location
     */
    @Override
    public void onLocationChanged(Location location) {
        coordinates.add(location);
        System.err.println("DEBUG - Location: Coordinates: Latiude: " + location.getLatitude() + "|| Longitude: " + location.getLongitude());
    }

    /*
     * @Description:    Override methods from LocationListener
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Empty
    }

    /*
     * @Description:    Override methods from LocationListener
     */
    @Override
    public void onProviderEnabled(String provider) {
        //Empty
    }

    /*
     * @Description:    Override methods from LocationListener
     */
    @Override
    public void onProviderDisabled(String provider) {
        //Empty
    }
}
