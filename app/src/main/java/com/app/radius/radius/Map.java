package com.app.radius.radius;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.Popup;
import com.esri.core.map.Graphic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Map.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Map newInstance() {
        Map fragment = new Map();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    public Map() {
        // Required empty public constructor
    }

    MapView map = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewer = inflater.inflate(R.layout.fragment_map, container, false);
        map = (MapView) viewer.findViewById(R.id.map);
        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == map && status == STATUS.INITIALIZED) {// Retrieve the map from XML layout
                    String mapURL = "https://services6.arcgis.com/vqLNlhGdMa1N5XDl/arcgis/rest/services/Carl_POI/FeatureServer/0";
                    // Add dynamic layer to MapView
                    ArcGISFeatureLayer dynamicLayer = new ArcGISFeatureLayer(mapURL, ArcGISFeatureLayer.MODE.ONDEMAND);

                    map.addLayer(dynamicLayer);
                    LocationDisplayManager loc = map.getLocationDisplayManager();
                    loc.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    loc.start();

                }
            }
        });
        /*LocationManager locman = (LocationManager)  this.getActivity().getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        if (locman.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (ContextCompat.checkSelfPermission(this.getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getActivity().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            }
            currentLocation = locman.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.w("Radius", "" + currentLocation.getLatitude() + currentLocation.getLongitude());
        }
        map.center
        map.centerAt(currentLocation.getLatitude(), currentLocation.getLongitude(), true);*/
        return viewer;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /*public void LocationUpdate(Location location) {
        currentLocation = location;
        map.centerAt(location.getLatitude(), location.getLongitude(), true);
    }*/

}
