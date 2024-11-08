package org.intelehealth.videolibrary.player.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import org.intelehealth.videolibrary.constants.Constants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import org.intelehealth.videolibrary.databinding.ActivityVideoPlayerBinding

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class VideoPlayerActivity : AppCompatActivity() {

    private var binding: ActivityVideoPlayerBinding? = null
    private var youtubePlayer: YouTubePlayer? = null

    private var isFullScreen: Boolean = false
    private var videoId: String? = null

    private var onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullScreen) {
                youtubePlayer?.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        videoId = intent.getStringExtra(Constants.VIDEO_ID)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding?.youtubePlayerView?.let {
            lifecycle.addObserver(it)
            it.enableAutomaticInitialization = false
        }

        initializeUI()
        playYouTubeVideo()
        addFullScreenListener()
    }

    private fun initializeUI() {
        // Status Bar Charges
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        // Back button functionality
        binding?.ivBack?.setOnClickListener {
            finish()
        }
    }

    private fun playYouTubeVideo() {
        videoId?.let {
            binding?.youtubePlayerView?.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    this@VideoPlayerActivity.youtubePlayer = youTubePlayer
                    youTubePlayer.loadVideo(it, 0f)
                }
            }, iFramePlayerOptions)
        }
    }

    private fun addFullScreenListener() {
        binding?.youtubePlayerView?.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullScreen = true
                binding?.youtubePlayerView?.visibility = View.GONE
                binding?.fullScreenViewContainer?.visibility = View.VISIBLE
                binding?.fullScreenViewContainer?.addView(fullscreenView)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onExitFullscreen() {
                isFullScreen = false
                binding?.youtubePlayerView?.visibility = View.VISIBLE
                binding?.fullScreenViewContainer?.visibility = View.GONE
                binding?.fullScreenViewContainer?.removeAllViews()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.itemId
        if (itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val iFramePlayerOptions: IFramePlayerOptions =
            IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()
    }
}