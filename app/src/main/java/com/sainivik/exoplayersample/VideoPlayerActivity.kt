package com.sainivik.exoplayersample

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sainivik.`interface`.OptionClickListener
import com.sainivik.adapter.SelectLanguageAdapter
import com.sainivik.exoplayersample.databinding.ActivityVideoPlayerBinding
import com.sainivik.model.LanguageModel
import com.sainivik.utils.Alerts


class VideoPlayerActivity : Activity(), Player.EventListener {

    private lateinit var dialog: Dialog
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private lateinit var binding: ActivityVideoPlayerBinding
    var backDoubleClick = false
    var forwardDoubleClick = false
    lateinit var adapter: SelectLanguageAdapter
    lateinit var trackSelector: DefaultTrackSelector

    var languageList = ArrayList<LanguageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        initializePlayer()
        setAdapter()

    }

    private fun setAdapter() {
        adapter = SelectLanguageAdapter(languageList, object : OptionClickListener {
            override fun click(pos: Int, data: LanguageModel) {
                trackSelector.parameters = trackSelector
                    .buildUponParameters()
                    .setMaxVideoSizeSd()
                    .setPreferredAudioLanguage(data.language)
                    .build();
                Toast.makeText(
                    this@VideoPlayerActivity,
                    "${data.label} is selected.",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
        })
    }


    /*setting click listeners*/
    private fun setListener() {
        binding.btnChangAudio.setOnClickListener {
            dialog = Alerts.changeLanguageDialog(this, adapter)

        }

        binding.btnBack.setOnClickListener {
            seekToBackward();
        }
        binding.btnForward.setOnClickListener {
            seekToFordward();
        }
    }

    /*this method is used to forward video for 5 sec */
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


    /*this method is used to backward video for 5 sec */

    private fun seekToBackward() {

        if (backDoubleClick && simpleExoPlayer.currentPosition >= 5000) {
            binding.showRewind = true
            Handler().postDelayed({ binding.showRewind = false }, 1000)
            simpleExoPlayer.seekTo((simpleExoPlayer.currentPosition - 5000))
            return
        }
        backDoubleClick = true
        Handler().postDelayed({ backDoubleClick = false }, 1000)
    }

    private fun initializePlayer() {
        trackSelector = DefaultTrackSelector(this)
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

        val mediaSourceFactory: MediaSourceFactory =
            DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .setTrackSelector(trackSelector)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.playWhenReady = true
        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()
        simpleExoPlayer.addListener(this)


    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            getAudioTrack()
        }
    }

    /*method is used to release player*/
    private fun releasePlayer() {
        simpleExoPlayer.release()
    }


    public override fun onStop() {
        super.onStop()
        simpleExoPlayer?.pause()

    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()

    }

    companion object {
        const val STREAM_URL = "https://bitmovin-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    }


    /*method is used to get available audio track in current video*/
    private fun getAudioTrack() {
        languageList.clear();
        for (i in 0 until simpleExoPlayer.currentTrackGroups.length) {
            val format: String =
                simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).sampleMimeType
                    ?: continue
            val lang: String? =
                simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).language ?: continue

            val label: String? =
                simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).label ?: continue

            val id: String? =
                simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).id ?: continue

            if (format!!.contains("audio") && id != null && lang != null) {
                languageList.add(LanguageModel(lang!!, label = label!!))
            }
        }

    }

}