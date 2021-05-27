package com.yudiz.e_cigarette.main.brand_listmodule

import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.yudiz.BR
import com.yudiz.databinding.ActivityBrandsDetailBinding
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_IMAGE
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_VIDEO
import com.yudiz.e_cigarette.AppConstants.Communication.BundleData.BRAND_ITEM_ID
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.BrandItemResponse
import com.yudiz.e_cigarette.data.model.response.Juice
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.helper.util.rvutil.RvUtil
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrandsDetailAct :
    BaseAct<ActivityBrandsDetailBinding, HomeVM>(Layouts.activity_brands_detail) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private lateinit var brandAdapter: BaseRvBindingAdapter<Juice>
    private var player: SimpleExoPlayer? = null
    lateinit var rvUtil: RvUtil

    override fun init() {
        val id = intent.getSerializableExtra(BRAND_ITEM_ID) as String
        vm.getBrandData(id)
        initBrandRecyclerView()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.cardClose -> {
                finish()
            }
        }
    }

    private fun initBrandRecyclerView() {
        brandAdapter = BaseRvBindingAdapter(
            Layouts.raw_brand_detail_list,
            mutableListOf(),
            br = BR.data
        )
        binding.rvBrandDetailList.adapter = brandAdapter
    }

    //exo player
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

        binding.brandAdsPlayer.player = player

        val mediaItem: MediaItem = vm.brandData.value?.data?.ads.let { MediaItem.fromUri(it!!) }

        player?.apply {
            repeatMode = Player.REPEAT_MODE_ALL
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
            volume = 0f
        }
    }

    private fun setViews() {
        binding.data = vm.brandData.value
        binding.tvLogo.visibility = View.VISIBLE
        binding.tvBrandDetails.visibility = View.VISIBLE
        binding.cardClose.visibility = View.VISIBLE
    }

    private fun formatType(type: String?) {
        when (type) {
            TYPE_VIDEO -> {
                binding.brandAdsPlayer.visibility = View.VISIBLE
                initializePlayer()
                initListeners()
            }
            TYPE_IMAGE -> {
                binding.ivAdsViewer.visibility = View.VISIBLE
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is BrandItemResponse) {

                    setViews()

                    apiRenderState.result.data?.juices?.let {
                        brandAdapter.addData(
                            it,
                            isClear = true
                        )
                    }
                    formatType(vm.brandData.value?.data?.type)
                }
                hideProgress()
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