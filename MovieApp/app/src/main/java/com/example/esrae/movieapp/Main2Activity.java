package com.example.esrae.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    long id;
    DatabaseHelper myDb;
    Movie movie;
    List<Trailer> Lst;
    ListView gwt;
    List<reviews> Lst1;
    ListView gwt1;
    ReviewsAdapter Vadapter1;
    TralierAdapter Tadapter;
    ImageView iv;
    @Override
    public void onStart() {
        super.onStart();
        if(isOnline()){
        Main2Activity.Asy movieTask = new Main2Activity.Asy();
            movieTask.execute();
            Main2Activity.Asy1 movieTask1 = new Main2Activity.Asy1();
            movieTask1.execute();
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Movie")) {
            movie = (Movie) intent.getSerializableExtra("Movie");
            ((TextView)findViewById(R.id.movie_title)).setText(movie.getTitle());
            ((TextView)findViewById(R.id.movie_rating)).setText(movie.getRate());
            ((TextView)findViewById(R.id.movie_overview)).setText(movie.getYear());
            ((TextView)findViewById(R.id.movie_release_date)).setText(movie.getDesc());
            ImageView imageView = (ImageView) findViewById(R.id.item);
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w185/"+movie.getPoster())
                    .into(imageView);
            id =  Long.parseLong(movie.getMovieId(),10);
        }

       // ImageButton ib= () findViewById(R.id.iig);
        //ib.setOnClickListener(ib.setBackgroundColor(0));


        gwt= (ListView) findViewById(R.id.lstviewTr);
        gwt1= (ListView) findViewById(R.id.lstviewRv);

        Lst =new ArrayList<>();
        Lst1 =new ArrayList<>();

        Tadapter= new TralierAdapter(Lst,this);
        Vadapter1= new ReviewsAdapter(Lst1,this);

        gwt.setAdapter(Tadapter);
        gwt1.setAdapter(Vadapter1);
        gwt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + Lst.get(position).getYouId()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + Lst.get(position).getYouId()));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        gwt.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myDb = new DatabaseHelper(Main2Activity.this);
        final ToggleButton toggleButton ;
        toggleButton = (ToggleButton) findViewById(R.id.iig);
//        toggleButton.setChecked(false);
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("Movie");
        if(myDb.getSpData(movie.getMovieId()).getCount()==0)
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_empty));
        else
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_full));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_full));
                    boolean isInserted = myDb.insertData(movie.getMovieId(),
                            movie.getTitle(), movie.getYear(), movie.getRate(),
                            movie.getDesc(), movie.getPoster());
                } else {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_star_empty));
                    myDb.deleteData(id + "");
                }
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    class Asy extends AsyncTask<String,Void,List<Trailer> > {

            @Override

            protected List<Trailer> doInBackground(String... params) {
                Intent intent = getIntent();
                if (intent != null && intent.hasExtra("Movie")) {
                    movie = (Movie) intent.getSerializableExtra("Movie");
                    id =  Long.parseLong(movie.getMovieId(),10);

                }
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

// Will contain the raw JSON response as a string.
                String Movieapp = null;

                String appId = "e2a4d00b2cd04f026c7700c807660bde";
                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are available at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    //http://api.themoviedb.org/3/movie/259316/videos?api_key=e2a4d00b2cd04f026c7700c807660bde
                    final String Movie_url = "http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=";
                    Uri builtUri = Uri.parse(Movie_url+appId).buildUpon().build();

                    URL url = new URL(builtUri.toString());

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        // Nothing to do.
                        Movieapp = null;
                        Log.e("esraa","ERROR HAHAHAHAHA");
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        Movieapp = null;
                    }
                    Movieapp = buffer.toString();
                } catch (IOException e) {
                    Log.e("ForecastFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attempting
                    // to parse it.
                    Movieapp = null;
                } finally{
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("ForecastFragment", "Error closing stream", e);
                        }
                    }
                }
                try {
                    return getMovieDataFromJson(Movieapp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }


            /**
             * Take the String representing the complete forecast in JSON Format and
             * pull out the data we need to construct the Strings needed for the wireframes.
             *
             * Fortunately parsing is easy:  constructor takes the JSON string and converts it
             * into an Object hierarchy for us.
             */
            private List<Trailer> getMovieDataFromJson(String Title)
                    throws JSONException {

                // These are the names of the JSON objects that need to be extracted.
                final String id="key";

                JSONObject jobject = new JSONObject(Title);
                JSONArray MovieArray = jobject.getJSONArray("results");

                List<Trailer> resultStrs = new ArrayList<>();

                for(int i = 0; i < MovieArray.length(); i++) {

                    JSONObject urls = MovieArray.getJSONObject(i);

                    Trailer film =new Trailer(
                            urls.getString(id)
                    );
                    resultStrs.add(film);
                }
                return resultStrs;
            }

            @Override
            protected void onPostExecute(List<Trailer> strings) {
                if (strings != null){
                    Lst.clear();
                }
                Lst=strings;
                gwt.setAdapter(new TralierAdapter(strings, Main2Activity.this));
            }
        }





    class Asy1 extends AsyncTask<String,Void,List<reviews> > {

        @Override

        protected List<reviews> doInBackground(String... params) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("Movie")) {
                movie = (Movie) intent.getSerializableExtra("Movie");
                id =  Long.parseLong(movie.getMovieId(),10);

            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String Movieapp = null;

            String appId = "e2a4d00b2cd04f026c7700c807660bde";
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#
                final String Movie_url = "http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=";

                Uri builtUri = Uri.parse(Movie_url+appId).buildUpon().build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    Movieapp = null;
                    Log.e("esraa","ERROR HAHAHAHAHA");
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Movieapp = null;
                }
                Movieapp = buffer.toString();
            } catch (IOException e) {
                Log.e("ForecastFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                Movieapp = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(Movieapp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private List<reviews> getMovieDataFromJson(String Title)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String id="content";

            JSONObject jobject = new JSONObject(Title);
            JSONArray MovieArray = jobject.getJSONArray("results");

            List<reviews> resultStrs = new ArrayList<>();

            for(int i = 0; i < MovieArray.length(); i++) {

                JSONObject urls = MovieArray.getJSONObject(i);

                reviews film =new reviews(
                        urls.getString(id)
                );
                resultStrs.add(film);
            }
            return resultStrs;
        }

        @Override
        protected void onPostExecute(List<reviews> strings) {
            if (strings != null){
                Lst1.clear();
            }
            Lst1=strings;
            gwt1.setAdapter(new ReviewsAdapter(strings, Main2Activity.this));
        }
    }





}
