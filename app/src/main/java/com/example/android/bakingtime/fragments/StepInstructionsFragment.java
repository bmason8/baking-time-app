package com.example.android.bakingtime.fragments;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Steps;
import com.example.android.bakingtime.utilities.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepInstructionsFragment extends Fragment {

    @OnClick(R.id.previous_button)
    void previousStepClick() {
                previousStep();
    }

    @OnClick(R.id.next_button)
    void nextStepClick() {
        nextStep();
    }

    @BindView(R.id.button_container)
    LinearLayout mButtonContainer;

    @BindView(R.id.simpleExoPlayerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.short_description)
    TextView mShortDescription;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.thumbnail_image)
    ImageView mThumbnailImageView;
    @BindView(R.id.button_container_cdv)
    CardView mButtonCardView;
    @BindView(R.id.exo_player_frame)
    FrameLayout mExoPlayerFrame;

    private SimpleExoPlayer mExoPlayer;
    private Steps mInstructions;
    private List<Steps> mRecipeSteps;
    int position;
    long videoPlayBackPosition;
    boolean mTwoPane, mSplitView;
    boolean mAutoPlay = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = 0;
        if (savedInstanceState != null) {
            mRecipeSteps = (List<Steps>) savedInstanceState.getSerializable("recipeSteps");
            mInstructions = savedInstanceState.getParcelable("stepInstructions");
            position = savedInstanceState.getInt("position");
            mSplitView = savedInstanceState.getBoolean("splitView");
            videoPlayBackPosition = savedInstanceState.getLong("playbackPosition");
            mAutoPlay = savedInstanceState.getBoolean("autoPlay", true);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTwoPane = bundle.getBoolean("twoPane", false);
                mRecipeSteps = bundle.getParcelableArrayList("recipeSteps");
                mSplitView = bundle.getBoolean("splitView", false);

                if (!mTwoPane) {
                    // LANDSCAPE/TABLET MODE
                    position = bundle.getInt("position");
                } else {
                    // PORTRAIT MODE
                    position = bundle.getInt("position");
                }
                mInstructions = mRecipeSteps.get(position);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_instructions_layout, container, false);
        ButterKnife.bind(this, rootView);

        determineLayoutParameters(mSplitView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            updateData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            updateData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            videoPlayBackPosition = mExoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setUseController(true);
            if (videoPlayBackPosition > 0) {
                mExoPlayer.seekTo(videoPlayBackPosition);
            }
            mPlayerView.setPlayer(mExoPlayer);
            // true plays video right away, false requires pressing play
            mExoPlayer.setPlayWhenReady(mAutoPlay);
            // Prepare the MediaSource
            String userAgent = "player";
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            videoPlayBackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mAutoPlay = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateData() {
        mShortDescription.setText(mInstructions.getShortDescription());
        mDescription.setText(mInstructions.getDescription());

        String thumbnailPath = mInstructions.getThumbnailURL();
        if (!TextUtils.isEmpty(thumbnailPath)) {
            // I removed the error and placeholder options on this method because I didn't want anything showing if there wasn't a valid image.
            Picasso.get()
                    .load(thumbnailPath)
                    .into(mThumbnailImageView);
        }

        String videoUrl = mInstructions.getVideoURL();
        if (!videoUrl.isEmpty()) {
            initializePlayer(NetworkUtils.convertStringToUri(mInstructions.getVideoURL()));
        } else {
            // replace exoPlayer with image
            releasePlayer();
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setUseController(false);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video));
            mPlayerView.setPlayer(mExoPlayer);
//            Toast.makeText(getContext(), String.valueOf(mPlayWhenReady) + " update", Toast.LENGTH_SHORT).show();
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void previousStep() {
        if (position > 0) {
            position -= 1;
        }
        mInstructions = mRecipeSteps.get(position);
        releasePlayer();
        videoPlayBackPosition = 0;
        updateData();
    }

    private void nextStep() {
        if (position < mRecipeSteps.size()-1) {
            position += 1;
        }
        mInstructions = mRecipeSteps.get(position);
        releasePlayer();
        videoPlayBackPosition = 0;
        updateData();
    }

    public void updatePosition(int newPosition) {
        position = newPosition;
        mInstructions = mRecipeSteps.get(position);
        releasePlayer();
        updateData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipeSteps", (Serializable) mRecipeSteps);
        outState.putParcelable("stepInstructions", mInstructions);
        outState.putInt("position", position);
        outState.putBoolean("splitView", mSplitView);
        outState.putLong("playbackPosition", videoPlayBackPosition);
        outState.putBoolean("autoPlay", mAutoPlay);
    }

// https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme
    private void determineLayoutParameters(boolean splitScreen) {
        if (splitScreen) {
            mButtonCardView.setVisibility(View.GONE);
        } else {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mButtonCardView.setVisibility(View.GONE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                }
            } else {
                mButtonCardView.setVisibility(View.VISIBLE);
            }
        }
    }
}
