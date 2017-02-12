package com.example.joane14.midtermexam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private List<Album> albums;

    private SearchView mSearchViewAlbum;
    private AlbumAdapter albumAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albums = new ArrayList<Album>();
        mSearchViewAlbum = (SearchView) findViewById(R.id.svSearch);
        mSearchViewAlbum.setOnQueryTextListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumAdapter = new AlbumAdapter(getApplicationContext(), albums);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(albumAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.clear: {
           if(item.getItemId()==R.id.clear) {
               findViewById(R.id.tvNo).setVisibility(View.VISIBLE);
               mSearchViewAlbum.setQuery("", false);
               mSearchViewAlbum.clearFocus();
//               findViewById(R.id.recycler_view).setVisibility(View.GONE);
                clear();
           }

//                clear();
//                break;
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    public void clear() {
        int size = this.albums.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.albums.remove(0);
            }
            albumAdapter.notifyItemRangeRemoved(0, size);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        SearchTask searchTask = new SearchTask(MainActivity.this);
        searchTask.execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class SearchTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;
        private Context context;
        private int added;
        boolean error = false;

        public SearchTask(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
        }


        protected void onPreExecute() {
            albumAdapter.notifyDataSetChanged();
            dialog.setMessage("Searching");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            albums.clear();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (error) {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            albumAdapter.notifyDataSetChanged();
            findViewById(R.id.tvNo).setVisibility(View.INVISIBLE);
//            findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);

//            else{

//            }
        }

        protected Boolean doInBackground(final String... args) {

            boolean flag = true;
            try {
                String queryValue = args[0].replace(" ", "%20");
                String urlForSearch ="http://ws.audioscrobbler.com/2.0/?method=album.search&album=" + queryValue + "&api_key=220b24b1490e374d3845b966fa7f0076&format=json";
                try {
                    HttpClient client = new DefaultHttpClient();
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    HttpPost request = new HttpPost(urlForSearch);


                    String httpResponse = client.execute(request, handler);


                    JSONObject finalResult = new JSONObject(httpResponse);
                    JSONObject temp = finalResult.getJSONObject("results");
                    JSONObject temp2 = temp.getJSONObject("albummatches");
                    JSONArray lists = temp2.getJSONArray("album");

                    for (int init = 0; init < lists.length(); init++) {
                        Album album = new Album();
                        temp = lists.getJSONObject(init);
                        album.setAlbumName(temp.getString("name"));
                        album.setArtist(temp.getString("artist"));
                        album.setUrl(temp.getString("url"));
                        JSONArray images = temp.getJSONArray("image");
                        album.setImageUrl(images.getJSONObject(1).getString("#text"));
                        albums.add(album);
                        Log.d("name", albums.get(init).getAlbumName());
                        Log.d("artist", albums.get(init).getArtist());
                        Log.d("image url", albums.get(init).getImageUrl());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    error = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("List size", Integer.toString(albums.size()));
            return true;
        }
    }
}
