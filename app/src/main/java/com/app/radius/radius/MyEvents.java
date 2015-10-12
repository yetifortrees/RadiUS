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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEvents extends Fragment {

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
    public static MyEvents newInstance(ArrayList<String> list) {
        MyEvents fragment = new MyEvents();
        Bundle b = new Bundle();
        b.putStringArrayList("Events", list);
        fragment.setArguments(b);
        return fragment;
    }

    public MyEvents() {
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
        final View view =  inflater.inflate(R.layout.fragment_my_events, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listMyEvents);
        Bundle args = getArguments();
        events = args.getStringArrayList("Events");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                events);
        listView.setAdapter(adapter);
        final Button button = (Button) view.findViewById(R.id.createEvent);
        final Toolbar title = (Toolbar) view.findViewById(R.id.toolbar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new CreateEvent();
                //title.setTitle("RadiUS - Create Event");
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.layout, fragment).commit();
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
