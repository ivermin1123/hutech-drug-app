package com.example.hutechdrugapp.ui.store;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hutechdrugapp.Entity.GetNearbyPlacesData;
import com.example.hutechdrugapp.MapActivity;
import com.example.hutechdrugapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class StoreFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    Location currentLocation;
    Button btnSearch,btnDirect;
    Polyline currentPolyline=null;
    EditText edtStartDes,edtEndDes;
    private MarkerOptions place1, place2;

    int PROXIMITY_RADIUS=5000;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private StoreViewModel storeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storeViewModel =
                ViewModelProviders.of(this).get(StoreViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_store, container, false);
      //  final TextView textView = root.findViewById(R.id.text_store);
        storeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });


        btnSearch=root.findViewById(R.id.btnSearchStore);
//
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fetchLastLocation();


        }catch (Exception e)
        {
            Log.d("Map",e.toString());
        }




        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                //map.clear();
                String hospital="drugstore";
                String url=getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(),hospital);
                Object dataTransfer[]=new Object[2];
                dataTransfer[0]=map;
                dataTransfer[1]=url;
                GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getContext(),"showing nearby drug",Toast.LENGTH_LONG).show();

            }


        });
        return root;
    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap);

                    supportMapFragment.getMapAsync(StoreFragment.this);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

        LatLng latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(" i m here"))
                .setIcon(bitmapDescriptor(getContext(),R.drawable.ic_baseline_accessibility_new_24));

    }


    private BitmapDescriptor bitmapDescriptor(Context context, int VectorResId){
        Drawable vectorDrawable= ContextCompat.getDrawable(context,VectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }
    private String getUrl(double latitude,double longitude,String nearbyPlace){
        //final String x="AIzaSyBgRTw6KPFWNV0Sr61V8pbAVvJDpLv3JOU";
        StringBuilder googlePlaceUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.map_api_key));





        return googlePlaceUrl.toString();
    }

}