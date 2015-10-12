package com.app.radius.radius;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsAttended extends Fragment {

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
    public static MyEvents newInstance(ArrayList<String> list, ArrayList<JSONObject> backEnd) {
        MyEvents fragment = new MyEvents();
        Bundle b = new Bundle();
        ArrayList<String> back = new ArrayList<>();
        for (int i = 0; i < backEnd.size(); i++) {
            back.add(backEnd.toString());
        }
        b.putStringArrayList("back", back);
        b.putStringArrayList("Events", list);
        fragment.setArguments(b);
        return fragment;
    }

    public EventsAttended() {
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
        View view =  inflater.inflate(R.layout.fragment_my_events, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listMyEvents);
        Bundle args = getArguments();
        events = args.getStringArrayList("Events");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.w("Radius:", "we pushed some shit");
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

    private void clickOnListItem(int position) {
        Log.w("Radius:", "list item " + position + " clicked.");
    }

}
