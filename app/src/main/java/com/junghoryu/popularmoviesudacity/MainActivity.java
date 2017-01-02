package com.junghoryu.popularmoviesudacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetMovieJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener
{
    private static final String TAG = "MainActivity";
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    private String sortingPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        sortingPreference = getString(R.string.popular);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(mMovieRecyclerViewAdapter);
    }




    @Override
    protected void onResume() {
        Log.d(TAG, "onResume starts");
        super.onResume();
        GetMovieJsonData getMovieJsonData = new GetMovieJsonData(this, getString(R.string.get_movie_base_url), getString(R.string.api_key), getString(R.string.get_movie_list_base_url), getString(R.string.poster_size_path));
        getMovieJsonData.execute(sortingPreference);
        Log.d(TAG, "onResume ends");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.popular_sort:
                sortingPreference = getString(R.string.popular);
                break;
            case R.id.highest_rate_sort:
                sortingPreference = getString(R.string.top_rated);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        GetMovieJsonData getMovieJsonData = new GetMovieJsonData(this, getString(R.string.get_movie_base_url), getString(R.string.api_key), getString(R.string.get_movie_list_base_url), getString(R.string.poster_size_path));
        getMovieJsonData.execute(sortingPreference);

        return true;
    }


    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){

        if(status==DownloadStatus.OK) {
            mMovieRecyclerViewAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "onDownloadComplete:  failed with status " + status);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mMovieRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

}
