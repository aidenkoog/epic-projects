package io.github.aidenkoog.android_wear_os.presentation.home.fragment.feature

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import io.github.aidenkoog.android_wear_os.BR
import io.github.aidenkoog.android_wear_os.R
import io.github.aidenkoog.android_wear_os.databinding.FragmentSleepBinding
import io.github.aidenkoog.android_wear_os.presentation.base.fragment.BaseFragment
import io.github.aidenkoog.android_wear_os.presentation.home.viewmodel.feature.SleepViewModel
import io.github.aidenkoog.android_wear_os.utils.utils.LottieUtil
import io.github.aidenkoog.android_wear_os.utils.utils.NavigationUtil

@AndroidEntryPoint
class SleepFragment : BaseFragment() {
    private var viewDataBinding: FragmentSleepBinding? = null
    private val viewModelData: SleepViewModel? by viewModels()

    private lateinit var loadingLottieView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSleepBinding.inflate(inflater, container, false)
        viewDataBinding?.setVariable(BR.sleepViewModel, viewModelData)
        viewDataBinding?.executePendingBindings()
        loadingLottieView = viewDataBinding?.loadingLottieView!!

        registerBackPressedCallback()
        return viewDataBinding?.root!!
    }

    override fun onHandleBackPressed() {
        super.onHandleBackPressed()
        handleBackPress()
    }

    private fun handleBackPress() {
        NavigationUtil.popBackStack(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("onViewCreated:")
        startLottieAnimation()
    }

    private fun startLottieAnimation() {
        LottieUtil.setLottieRawResource(loadingLottieView, R.raw.setting_loading)
        LottieUtil.setLottieSpeed(loadingLottieView, 1.2f)
        LottieUtil.addLottieAnimatorListener(loadingLottieView, lottieAnimationCallback)
        LottieUtil.playLottie(loadingLottieView)
    }

    private val lottieAnimationCallback = object : LottieUtil.LottieAnimatorListener {
        override fun onAnimationStart(animator: Animator?) {
            Logger.i("Started animation")
        }

        override fun onAnimationEnd(animator: Animator?) {
            Logger.i("End animation")
        }

        override fun onAnimationCancel(animator: Animator?) {
            Logger.i("Canceled animation")
        }

        override fun onAnimationRepeat(animator: Animator?) {
            Logger.i("Repeated animation")
        }

    }

    private fun setDataObserver() {
        viewModelData?.isLoaded?.observe(this) {
            if (it == true) {
                Logger.i("Loaded!")
            }
        }
    }
}