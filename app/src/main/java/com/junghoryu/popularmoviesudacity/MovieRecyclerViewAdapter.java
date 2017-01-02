package com.junghoryu.popularmoviesudacity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jryu075 on 2016-12-21.
 */

class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieImageViewHolder> {
    private static final String TAG = "MovieRecyclerViewAdapte";
    private List<Photo> mPhotoList;
    private Context mContext;

    public MovieRecyclerViewAdapter(Context context, List<Photo> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    @Override
    public MovieImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MovieImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieImageViewHolder holder, int position) {
        //called by the layout manager when it wants new data in an existing row.

        Photo photoItem = mPhotoList.get(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " --> " + position);
        Picasso.with(mContext).load(photoItem.getPosterPath())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return ((mPhotoList != null) && (mPhotoList.size() !=0) ? mPhotoList.size() : 0);
    }

    void loadNewData(List<Photo> newPhotos) {
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotoList != null) && (mPhotoList.size() !=0) ? mPhotoList.get(position) : null);
    }

    static class MovieImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieImageViewHolder";
        ImageView thumbnail = null;

        public MovieImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MovieImageViewHolder starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }
}
