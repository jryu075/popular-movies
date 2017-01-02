package com.junghoryu.popularmoviesudacity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null) {
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            photoTitle.setText(photo.getTitle());

            TextView photoOverview = (TextView) findViewById(R.id.photo_overview);
            photoOverview.setText(photo.getOverview());

            TextView photoReleaseDate = (TextView) findViewById(R.id.photo_release_date);
            photoReleaseDate.setText(getString(R.string.release_date) + photo.getReleaseDate());

            TextView photoVoteAverage = (TextView) findViewById(R.id.photo_vote_average);
            photoVoteAverage.setText(getString(R.string.vote_average) + photo.getVoteAverage());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);

            Picasso.with(this).load(photo.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
