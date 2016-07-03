package rs.ftn.pma.tourismobile.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.EActivity;

import java.util.List;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.MapUtils;

@EActivity
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if (MapUtils.isUpdated()) {
            final List<Destination> destinations = MapUtils.getDestinations();
            if (!destinations.isEmpty()) {
                LatLngBounds.Builder latLngBoundsBuilder = LatLngBounds.builder();
                for (Destination destination : destinations) {
                    LatLng position = new LatLng(destination.getLatitude(), destination.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(position).title(destination.getName()));
                    latLngBoundsBuilder.include(position);
                }

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height, padding));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                      @Override public boolean onMarkerClick(Marker marker) {
                          for (Destination destination : destinations) {
                              LatLng location = new LatLng(destination.getLatitude(), destination.getLongitude());
                              if (location.equals(marker.getPosition())) {
                                  DestinationDetailsActivity_.intent(MapsActivity.this)
                                          .destinationID(destination.getId())
                                          .start();
                                  return true;
                              }
                          }
                          return false;
                      }
                  }
                );
//            }
            }
            else {
                Location here = getLastLocation();
                if(here != null) {
                    LatLng latLng = new LatLng(here.getLatitude(), here.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
    }

    public Location getLastLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return mLastLocation;
        }
        else {
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return mLastLocation;
        }
    }
}
