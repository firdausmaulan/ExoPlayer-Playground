package com.pg.exo

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.pg.exo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), Player.EventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var player: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private val videos = ArrayList<String>()
    private lateinit var mainAdapter: MainAdapter
    private lateinit var selectedVideo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivPlayList.setOnClickListener {
            if (binding.rvVideos.isVisible) {
                binding.rvVideos.visibility = View.GONE
            } else {
                binding.rvVideos.visibility = View.VISIBLE
            }
        }

        initAdapter()

        selectedVideo = videos[0]
    }

    private fun initAdapter() {
        videos.add("https://html5demos.com/assets/dizzy.mp4")
        videos.add("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4")
        videos.add("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4")

        mainAdapter = MainAdapter(videos)
        mainAdapter.setListener(object : MainAdapter.Listener {
            override fun onItemClick(selected: String) {
                preparePlayer(selected)
                binding.rvVideos.visibility = View.GONE
            }
        })

        binding.rvVideos.apply {
            adapter = mainAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
        preparePlayer(selectedVideo)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        binding.exoplayerView.player = player
        player.seekTo(playbackPosition)
        player.playWhenReady = true
        player.addListener(this)
    }

    private fun preparePlayer(url: String) {
        val uri = Uri.parse(url)
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }

    private fun releasePlayer() {
        playbackPosition = player.currentPosition
        player.release()
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        // handle error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            binding.progressBar.visibility = View.VISIBLE
        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}