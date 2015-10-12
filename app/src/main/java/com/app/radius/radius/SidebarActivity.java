package com.app.radius.radius;

import android.app.Fragment;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class SidebarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean haveSwitched = false;
    Integer lastFrag = null;
    Integer currentFrag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fg = Map.newInstance();
        // adding fragment to relative layout by using layout id
        getFragmentManager().beginTransaction().add(R.id.layout, fg).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SharedPreferences settings = getSharedPreferences("Radius", 0);
        //String authkey = settings.getString("authkey", null);
        String data = null;
        GraphRequest request = GraphRequest.newMeRequest(
               AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        onGraphRequest(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture");
        request.setParameters(parameters);
        request.executeAsync();
        //Log.w("Radius",request.getGraphObject().toString());


    }

    private void onGraphRequest(JSONObject result) {
        Log.w("GraphRequest", result.toString());

        class PutUpImage extends AsyncTask<JSONObject, Bitmap, Bitmap> {
            JSONObject result;
            public PutUpImage (JSONObject j) {
                super();
                result = j;
            }
            protected Bitmap doInBackground(JSONObject... json) {
                String url = null;
                try {
                    url = result.getJSONObject("picture").getJSONObject("data").getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                URL photourl = null;
                try {
                    photourl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap mIcon_val = null;
                try {
                    mIcon_val = BitmapFactory.decodeStream(photourl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mIcon_val;
            }
            protected void onPostExecute(Bitmap result) {
                fillPicture(result);
            }
        }
        new PutUpImage(result).execute(result);
        TextView name = (TextView) findViewById(R.id.name);
        try {
            name.setText(result.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        };

    }

    private void fillPicture(Bitmap picture) {
        ImageView profile = (ImageView) findViewById(R.id.profilepicture);
        profile.setImageBitmap(picture);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (haveSwitched && (lastFrag != currentFrag)) {
                Toolbar title = (Toolbar) findViewById(R.id.toolbar);
                final ArrayList<String> list = new ArrayList<String>();
                String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                        "Android", "iPhone", "WindowsMobile"};
                for (int i = 0; i < values.length; ++i) {
                    list.add(values[i]);
                }
                switch (lastFrag) {
                    case 1:
                        Fragment fragment = new Map();
                        title.setTitle("RadiUS - Map");
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.layout, fragment).commit();
                        currentFrag = 1;
                        break;
                    case 2:
                        title.setTitle("RadiUS - My Events");
                        fragment = MyEvents.newInstance(list);
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.layout, fragment).commit();
                        currentFrag = 2;
                        break;
                    case 3:
                        title.setTitle("RadiUS - Local Events");
                        String url = "http://radius.mybluemix.net?action=3";
                        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                        StringRequest jsObjRequest = new StringRequest
                                (Request.Method.GET, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        switchEventsAttended(response);
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error:", "ERROR: " + error.getMessage());
                                    }
                                });
                        queue.add(jsObjRequest);
                        queue.start();
                        currentFrag = 3;
                        break;
                    case 4:
                        fragment = new Settings();
                        title.setTitle("RadiUS - Settings");
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.layout, fragment).commit();
                        currentFrag = 4;
                        break;
                    case 5:
                        currentFrag = 5;
                        break;
                }
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.sidebar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar title = (Toolbar) findViewById(R.id.toolbar);
        if (id == R.id.nav_myEvents) {
            haveSwitched = true;
            lastFrag = currentFrag;
            currentFrag = 2;
            title.setTitle("RadiUS - My Events");
            String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                    "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                    "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                    "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                    "Android", "iPhone", "WindowsMobile" };

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }
            Fragment fragment = MyEvents.newInstance(list);
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.layout, fragment).commit();

        } else if (id == R.id.nav_eventsAttend) {
            haveSwitched = true;
            lastFrag = currentFrag;
            currentFrag = 3;
            title.setTitle("RadiUS - Local Events");
            String url = "http://radius.mybluemix.net?action=3";
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            StringRequest jsObjRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            switchEventsAttended(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error:", "ERROR: " + error.getMessage());
                        }
                    });
            queue.add(jsObjRequest);
            queue.start();

        } else if (id == R.id.nav_settings) {
            haveSwitched = true;
            lastFrag = currentFrag;
            currentFrag = 4;
            Fragment fragment = new Settings();
            title.setTitle("RadiUS - Settings");
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.layout, fragment).commit();
        } else if (id == R.id.nav_map) {
            lastFrag = currentFrag;
            currentFrag = 1;
            haveSwitched = true;
            Fragment fragment = new Map();
            title.setTitle("RadiUS - Map");
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.layout, fragment).commit();
        } else if (id == R.id.nav_logout) {
            haveSwitched = true;
            lastFrag = currentFrag;
            currentFrag = 5;
            LoginManager.getInstance().logOut();
            Intent logoutSuccess = new Intent(this, LoginActivity.class);
            startActivity(logoutSuccess);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchEventsAttended(String response) {
        JSONObject events;
        ArrayList<String> eventsArray = new ArrayList<String>();
        ArrayList<JSONObject> backEnd = new ArrayList<JSONObject>();
        ArrayList<JSONObject> names = new ArrayList<>();
        try {
            events = new JSONObject(response);
            JSONArray stuff = events.getJSONArray("response");
            if (stuff != null) {
                //WHY ARYOU DOING A ROFSOTIRIDED LIST HAHAHAH
                for (int i=0;i<stuff.length()*2;i++){
                    eventsArray.add(stuff.getJSONObject(i).getString("title") + "\n" + stuff.getJSONObject(i).getString("owner"));
                    backEnd.add(stuff.getJSONObject(i));
                    //eventsArray.add(stuff.getJSONObject(i).getString("owner"));
                }
            }
        } catch (JSONException e) {

        }
        Fragment fragment = EventsAttended.newInstance(eventsArray, backEnd);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.layout, fragment).commit();

    }

}
