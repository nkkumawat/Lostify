package me.nkkumawat.lostify;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


/**
 * Created by sonu on 28/6/18.
 */

public class LocationFinder implements LocationListener {
    public String longitude ;
    public String latitude;
    @Override
    public void onLocationChanged(Location loc) {
         longitude = "Longitude: " + loc.getLongitude();

         latitude = "Latitude: " + loc.getLatitude();


//        /*------- To get city name from coordinates -------- */
//        String cityName = null;
//        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(loc.getLatitude(),
//                    loc.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                + cityName;
//        editLocation.setText(s);
    }



    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

