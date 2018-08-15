package com.example.android.bakingtime;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.button_container_cdv)
    CardView mButtonCardView;
    @BindView(R.id.exo_player_frame)
    FrameLayout mExoPlayerFrame;

    private SimpleExoPlayer mExoPlayer;
//    private SimpleExoPlayerView mPlayerView;
    private Steps mInstructions;
    private List<Steps> mRecipeSteps;
    int position;
    boolean mTwoPane, mSplitView;

//    public StepInstructionsFragment() {
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = 0;
        if (savedInstanceState != null) {
            mRecipeSteps = (List<Steps>) savedInstanceState.getSerializable("recipeSteps");
            mInstructions = savedInstanceState.getParcelable("stepInstructions");
            position = savedInstanceState.getInt("position");
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

        if (mSplitView) {
            mButtonCardView.setVisibility(View.GONE);
        } else {
            mButtonCardView.setVisibility(View.VISIBLE);
        }

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            mTwoPane = bundle.getBoolean("twoPane", false);
//            mRecipeSteps = bundle.getParcelableArrayList("recipeSteps");
//
//            if (!mTwoPane) {
//                // LANDSCAPE/TABLET MODE
//                position = bundle.getInt("position");
//            } else {
//                // PORTRAIT MODE
//                position = bundle.getInt("position");
//            }
//            mInstructions = mRecipeSteps.get(position);
//        }




//        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(
//                Picasso.with(getContext())
//                        .load(mInstructions.getThumbnailURL())
//                        .placeholder(R.drawable.ic_banana)
//                        .error(R.drawable.ic_banana)
//                        .into(mPlayerView);



        updateData();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {

        Log.d("video", "initializePlayer ran");
        // need to set up an if/else statement for replacing the default exoPlayer view with a thumbnail and no controls if there is no mediaUri

        if (mExoPlayer == null) {
            Log.d("video", "mExoplayer is null");
            // Create an instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setUseController(true);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource
            String userAgent = "temp";
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            // true plays video right away, false requires pressing play
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    private void updateData() {
        mShortDescription.setText(mInstructions.getShortDescription());
        mDescription.setText(mInstructions.getDescription());
        String videoUrl = mInstructions.getVideoURL();
        Log.d("videoURL ", "something: " + videoUrl);
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

        }
    }

    private void previousStep() {
        if (position > 0) {
            position -= 1;
        }
        mInstructions = mRecipeSteps.get(position);
        releasePlayer();
        updateData();
    }

    private void nextStep() {
        if (position < mRecipeSteps.size()-1) {
            position += 1;
        }
        mInstructions = mRecipeSteps.get(position);
        releasePlayer();
        updateData();
    }

//    protected void positionUpdate(int newPosition) {
//        Toast.makeText(getContext(), "positionUpdate Ran!", Toast.LENGTH_SHORT).show();
//        position = newPosition;
//        updateData();
//    }

    public void positionUpdateFromInterface(int newPosition) {
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
    }

    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        Toast.makeText(getContext(), "onConfigurationChanged", Toast.LENGTH_SHORT).show();
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mDescription.setVisibility(View.GONE);
//            mShortDescription.setVisibility(View.GONE);
//            mButtonCardView.setVisibility(View.GONE);
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mExoPlayerFrame.getLayoutParams();
//            params.width = LayoutParams.MATCH_PARENT;
//            params.height = LayoutParams.MATCH_PARENT;
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mDescription.setVisibility(View.VISIBLE);
//            mShortDescription.setVisibility(View.VISIBLE);
//            mButtonCardView.setVisibility(View.VISIBLE);
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mExoPlayerFrame.getLayoutParams();
//            params.width = LayoutParams.MATCH_PARENT;
//            params.height = LayoutParams.WRAP_CONTENT;
//        }
//    }

}
