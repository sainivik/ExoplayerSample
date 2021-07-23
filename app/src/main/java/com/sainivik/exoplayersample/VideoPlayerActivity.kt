package com.sainivik.exoplayersample

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sainivik.exoplayersample.databinding.ActivityVideoPlayerBinding


class VideoPlayerActivity : Activity() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private lateinit var binding: ActivityVideoPlayerBinding
    var backDoubleClick = false
    var forwardDoubleClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {

        binding.btnBack.setOnClickListener {
            seekToBackward();
        }
        binding.btnForward.setOnClickListener {
            seekToFordward();
        }
    }

    private fun seekToFordward() {
        if (forwardDoubleClick) {
            binding.showFordward = true
            Handler().postDelayed({ binding.showFordward = false }, 1000)
            simpleExoPlayer.seekTo((simpleExoPlayer!!.currentPosition + 5000).toLong())
            return
        }
        forwardDoubleClick = true
        Handler().postDelayed({ forwardDoubleClick = false }, 1000)

    }

    private fun seekToBackward() {

        if (backDoubleClick && simpleExoPlayer!!.currentPosition >= 5000) {
            binding.showRewind = true
            Handler().postDelayed({ binding.showRewind = false }, 1000)
            simpleExoPlayer.seekTo((simpleExoPlayer!!.currentPosition - 5000).toLong())
            return
        }
        backDoubleClick = true
        Handler().postDelayed({ backDoubleClick = false }, 1000)
    }

    private fun initializePlayer() {

        mediaDataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        var mediaSource =
            HlsMediaSource.Factory(
                DefaultHttpDataSourceFactory(
                    Util.getUserAgent(
                        this,
                        "mediaPlayerSample"
                    )
                )
            )
                .createMediaSource(MediaItem.fromUri(STREAM_URL))


        //  val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(STREAM_URL))

        val mediaSourceFactory: MediaSourceFactory =
            DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.playWhenReady = true
        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()

    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    companion object {
        //   const val STREAM_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        const val STREAM_URL = "https://bitmovin-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    }
}