package com.example.android.baking.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.baking.R;
import com.example.android.baking.data.parcelableclasses.Step;
import com.example.android.baking.databinding.FragmentRecipeInfoDetailBinding;
import com.example.android.baking.utils.AppUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class RecipeInfoDetailFragment extends Fragment {
    private FragmentRecipeInfoDetailBinding binding;
    private Step mStep;
    private SimpleExoPlayerView mExoPlayerView;

    private SimpleExoPlayer mExoPlayer;

    public RecipeInfoDetailFragment() {
    }

    private final String SELECTED_POSITION = "selected_position";
    private final String PLAY_WHEN_READY = "play_when_ready";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mStep = bundle.getParcelable(AppUtils.EXTRAS_STEP);
        }

        if (savedInstanceState != null){
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_info_detail, container, false);
        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        binding.tvStepShortDescription.setText(mStep.getShortDescription());
        binding.tvStepDescription.setText(mStep.getDescription());

        // General Case
        // 1 - If Video exists: Show Video
        // 2 - If Thumbnail exists: Show image view with the thumbnail where at the place where the video would normally show up.

        // If video exists, initialize player (General case 1)
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializerPlayer(Uri.parse(mStep.getVideoURL()));
        else if (!mStep.getThumbnailURL().isEmpty()) {
            // if Thumbnail exists but no video exists (General case 2)
            binding.thumbnailView.setVisibility(View.VISIBLE);
            binding.thumbnailView.setImageURI(mStep.getThumbnailURL());
        }
    }

    private void initializerPlayer(Uri mediaUri) {
        mExoPlayerView = binding.ExoplayerView;
        try {

            // bandWidthMeter is used for
            // getting default bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // track selector is used to navigate between
            // video using a default seekBar.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // we are creating a variable for dataSource factory
            // and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);

            // inside our exoplayer view
            // we are setting our player
            mExoPlayerView.setPlayer(mExoPlayer);

            // we are preparing our exoplayer
            // with media source.
            mExoPlayer.prepare(mediaSource);

            // we are setting our exoplayer
            // when it is ready.
            mExoPlayer.setPlayWhenReady(playWhenReady);

        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : " + e.toString());
        }
    }

    private boolean playWhenReady = true;
    private long position = -1;
    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null){
            position = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, position);
        outState.putBoolean(PLAY_WHEN_READY , playWhenReady);
    }
}

