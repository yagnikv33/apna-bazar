package com.esjayit.apnabazar.main.entrymodule.view

import android.graphics.Color
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.esjayit.databinding.ActivitySplashScreenBinding
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenAct :
    BaseAct<ActivitySplashScreenBinding, EntryVM>(Layouts.activity_splash_screen) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null

    override fun init() {
        vm.getSplashScreenData()
        initListeners()
        initializePlayer()

        //To store data in pref
        prefs.authToken = "auth token"
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.tvToHome -> {
                /** REDIRECTION CODE HERE */
            }
        }
    }

    private fun initViews() {
        binding.tvToHome.text = vm.splashData.value?.data?.title

        vm.splashData.value?.data?.backgroundColor?.let {
            binding.tvToHome.setBackgroundColor(
                Color.parseColor(
                    it
                )
            )
        }
    }

    private fun initListeners() {
        player?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                "ExoPlayer ${error.message}".logE()
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                "ExoPlayer $reason".logE()
            }
        })
    }

    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        binding.playerView.player = player

        val mediaItem: MediaItem = vm.splashData.value?.data?.video.let { MediaItem.fromUri(it!!) }

        player?.apply{

            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
            volume = 0f
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        initializePlayer()
    }

    private fun hideSystemUi() {
        binding.playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        player?.stop()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
        player?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is SplashResponse -> {

                        initViews()
                        initializePlayer()
                        hideProgress()
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
            }
            is ApiRenderState.ApiError<*> -> {
                hideProgress()
            }
        }
    }
}