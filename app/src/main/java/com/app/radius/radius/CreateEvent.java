package com.app.radius.radius;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEvent extends Fragment {

    ListAdapter adapter;
    ArrayList<String> listItems=new ArrayList<String>();

    private OnFragmentInteractionListener mListener;
    private ArrayList<String> events;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEvent newInstance(ArrayList<String> list) {
        CreateEvent fragment = new CreateEvent();
        Bundle b = new Bundle();
        b.putStringArrayList("Events", list);
        fragment.setArguments(b);
        return fragment;
    }

    public CreateEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_create_event, container, false);
        final Button submitNewEvent = (Button) view.findViewById(R.id.submit);
        submitNewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText eventName = (EditText) view.findViewById(R.id.EventName);
                EditText eventAddress = (EditText) view.findViewById(R.id.Address);
                EditText eventDate = (EditText) view.findViewById(R.id.Date);
                EditText eventTime = (EditText) view.findViewById(R.id.Time);
                EditText eventDescription = (EditText) view.findViewById(R.id.EventDescription);
                EditText eventTimeEnd = (EditText) view.findViewById(R.id.TimeEnd);
                String url = "";
                url = "http://radius.mybluemix.net?" + "action=0&title="+eventName.getText().toString()+"&owner="+ AccessToken.getCurrentAccessToken().getUserId()+"&address="+eventAddress.getText().toString()+"&startdate="+eventDate.getText().toString()+"&starttime="+eventTime.getText().toString()+"&endtime="+eventTimeEnd.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
                StringRequest jsObjRequest = new StringRequest
                        (Request.Method.GET, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.w("Req", "Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error:", "ERROR: " + error.getMessage());
                            }
                        });
                queue.add(jsObjRequest);
                queue.start();
                //@1:30AM
                //Forgive me father for I have sinned
                }

        });

        return view;
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

}
