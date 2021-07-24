# ExoplayerSample
Video streaming using exoplayer

### Screenshot
<img src="https://github.com/sainivik/ExoplayerSample/blob/master/app/screenshots/home.png" width="300px" height="632px"/>
<img src="https://github.com/sainivik/ExoplayerSample/blob/master/app/screenshots/language.png" width="300px" height="632px"/>

#### Usage

Add the following code to your view

```xml
      <com.google.android.exoplayer2.ui.PlayerView
                android:id="@+id/playerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginTop="100dp"
                android:focusable="true"/>
```

 the following code is used to initialize player

```java
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
```
the following code is used to switch video language

```java

 trackSelector.parameters = trackSelector
                    .buildUponParameters()
                    .setMaxVideoSizeSd()
                    .setPreferredAudioLanguage(data.language)
                    .build();
```



## Author

Vikas Saini

email: vikaskumarsaini0001@gmail.com


