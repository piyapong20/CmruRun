package devded.silkyland.cmrurun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double cmruLatADouble = 18.807093, cmruLngADouble = 98.98641;
    private double userLatADouble, userLngADouble;
    private LocationManager locationManager;
    private Criteria criteria;
    private String userIDString, userNameString,goldString;
    private static final String urlEditLocation = "http://swiftcodingthai.com/cmru/edit_location_master.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

        //Setup
        userLatADouble = cmruLatADouble;
        userLngADouble = cmruLngADouble;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        //Get Value From Intent
        userIDString = getIntent().getStringExtra("userID");
        userNameString = getIntent().getStringExtra("Name");
        goldString = getIntent().getStringExtra("Gold");

        Log.d("30JuneV1", "userID ==> " + userIDString);
        Log.d("30JuneV1", "userName ==> " + userNameString);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    } // Main Method

    private class SynLocation extends AsyncTask<Void, Void, String> {

        private static final String urlJON = "http://swiftcodingthai.com/cmru/get_user_master.php";
        private MyData myData;

        @Override
        protected String doInBackground(Void... voids) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("30JuneV1", "e doIn ==> " + e.toString());
                return null;
            }

        }   // doIn

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("30JuneV1", "JSON ==> " + s);

            myData = new MyData();
            int[] intIcon = myData.getAvatarInts();



            try {

                JSONArray jsonArray = new JSONArray(s);

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strName = jsonObject.getString("Name");
                    int iconMaker = intIcon[Integer.parseInt(jsonObject.getString("Avata"))];
                    double douLat = Double.parseDouble(jsonObject.getString("Lat"));
                    double douLng = Double.parseDouble(jsonObject.getString("Lng"));
                    LatLng latLng = new LatLng(douLat, douLng);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(iconMaker))
                            .title(strName));


                } //for

            } catch (Exception e) {
                Log.d("30JuneV1", "e onPost ==> " + e.toString());
            }

        }
    } // SynLocation Class

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);

        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {

            userLatADouble = networkLocation.getLatitude();
            userLngADouble = networkLocation.getLongitude();

        }   //if

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {

            userLatADouble = gpsLocation.getLatitude();
            userLngADouble = gpsLocation.getLongitude();

        }

        Log.d("29JuneV1", "userLat ==> " + userLatADouble);
        Log.d("29JuneV1", "userLat ==> " + userLngADouble);

    }   //onResume

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);


        } else {
            Log.d("29JuneV1", "Cannot Find Location");
        }

        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            userLatADouble = location.getLatitude();
            userLngADouble = location.getLongitude();

        }   //onLocationChang

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       //Setup to CMRU
        LatLng latLng = new LatLng(cmruLatADouble, cmruLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        createStationMaker();

        myLoop();

    } // onMapReady

    private void myLoop() {

        Log.d("29JuneV1","userLat ==> " + userLatADouble);
        Log.d("29JuneV1","userLng ==> " + userLngADouble);

        mMap.clear();

        createStationMaker();

        editLocation();

        checkDistance();

        SynLocation synLocation = new SynLocation();
        synLocation.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 3000);
    }

    private void checkDistance() {

        double douMyDistance =

    }   // checkDistance

    private void editLocation() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("id", userIDString)
                .add("Lat", Double.toString(userLatADouble))
                .add("Lng", Double.toString(userLngADouble))
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(urlEditLocation).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });

    }   // editLocation

    private void createStationMaker() {
        MyData myData = new MyData();
        double[] latDoubles = myData.getLatStationDoubles();
        double[] lngDoubles = myData.getLngStationDoubles();
        int[] iconInts = myData.getIconStationInts();

        for (int i=0;i<latDoubles.length;i++) {
            LatLng latLng = new LatLng(latDoubles[i], lngDoubles[i]);
            mMap.addMarker(new MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.fromResource(iconInts[i]))
            .title("ด่าน " + Integer.toString(i+1)));
        }
    } // create StationMaker


} // Main Class
