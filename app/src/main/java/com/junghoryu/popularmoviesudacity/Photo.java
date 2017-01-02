package com.junghoryu.popularmoviesudacity;

import java.io.Serializable;

/**
 * Created by jryu075 on 2016-12-19.
 */

class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mPosterPath;
    private String mOverView;
    private String mReleaseDate;
    private String mTitle;
    private float mVoteAverage;

    public Photo(String overView, String posterPath, String releaseDate, String title, float voteAverage) {

        mOverView = overView;
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mTitle = title;
        mVoteAverage = voteAverage;
    }


    public String getOverview() {
        return mOverView;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mOverView='" + mOverView + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mVoteAverage=" + mVoteAverage +
                '}';
    }
}
