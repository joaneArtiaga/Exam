package com.example.joane14.midtermexam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Joane14 on 12/02/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    private List<Album> list;
    Context context;


    public AlbumAdapter(Context context,List<Album> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.displaycard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("WWW", Integer.toString(list.size()));
        final Album album =list.get(position);
        holder.mBand.setText(album.getArtist());
        holder.mName.setText(album.getAlbumName());
        /*new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Bitmap bitmap = drawable_from_url(album.getImageUrl());

                    holder.image.setImageBitmap(bitmap);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
        new ImageAsyncTask(holder.image,album).execute();
    }

    private class ImageAsyncTask extends AsyncTask<Void,Void,Void>{

        private ImageView imageView;
        private Album album;

        public ImageAsyncTask(ImageView imageView, Album album){
            this.album = album;
            this.imageView = imageView;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                album.setImage(drawable_from_url(album.getImageUrl()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(album.getImage() !=null) {
                imageView.setImageBitmap(album.getImage());
            }
        }
    }


    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return x;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mName, mBand;
        ImageView image;
        public ViewHolder(View view){
            super(view);
            mName = (TextView) view.findViewById(R.id.tvSong);
            mBand= (TextView) view.findViewById(R.id.tvBand);
            image = (ImageView) view.findViewById(R.id.image1);
        }

    }
}
