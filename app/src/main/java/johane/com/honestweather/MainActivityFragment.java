package johane.com.honestweather;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import johane.com.honestweather.structures.OWMWeatherResponse;
import johane.com.honestweather.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * A placeholder fragment containing a simple view.
 */
@Slf4j
public class MainActivityFragment extends Fragment {

    // TODO : Avoid hardcoding APP_ID
    private static final String APP_ID = "eb89128c600ced1e6281ce2e7171af0e";
    private Gson gson = new Gson();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log.info("Creating fragment..");


        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        log.info("onConnected");
                        try {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);

                            getCurrentWeatherInfo(mLastLocation);

                        } catch (SecurityException e) {
                            // TODO : Use checkPermission and ask user
                            log.error("Failed to get location", e);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        log.info("Connection suspended i = " + i);
                    }
                };

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        log.error("Connection failed" + connectionResult);
                    }
                };

        buildGoogleApiClient(connectionCallbacks, connectionFailedListener);

        if(mGoogleApiClient!= null){
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient(
            final GoogleApiClient.ConnectionCallbacks connectionCallbacks,
            final GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();

        log.info("Resuming fragment..");
    }

    private void getCurrentWeatherInfo(final Location location) {

        if (location == null) { return; }

        String url = "http://api.openweathermap.org/data/2.5/weather?"
                + "lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude()
                + "&appid=" + APP_ID;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       log.info("Response: " + response.toString());

                        OWMWeatherResponse owmWeatherResponse = gson.fromJson(
                                response.toString(), OWMWeatherResponse.class);

                        Toast.makeText(getActivity(), owmWeatherResponse.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        log.error("Failure to get weather info", error);
                    }
                });
        RequestUtil.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
