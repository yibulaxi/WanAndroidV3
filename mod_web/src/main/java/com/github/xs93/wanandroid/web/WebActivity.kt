package com.github.xs93.wanandroid.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.core.text.htmlEncode
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ktx.addOnBackPressedCallback
import com.github.xs93.framework.ktx.isStatusBarTranslucentCompat
import com.github.xs93.wanandroid.web.databinding.WebActivityAgentWebBinding
import com.github.xs93.wanandroid.web.webclient.WebClientFactory
import com.google.android.material.color.MaterialColors
import com.google.android.material.internal.ToolbarUtils
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient

/**
 * WebView显示界面
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/4 12:00
 * @email 466911254@qq.com
 */
class WebActivity :
    BaseViewBindingActivity<WebActivityAgentWebBinding>(
        R.layout.web_activity_agent_web,
        WebActivityAgentWebBinding::bind
    ) {

    companion object {

        private const val PARAMS_URL = "params_url"
        private const val PARAMS_TITLE = "params_title"

        @JvmStatic
        fun start(context: Context, url: String, title: String? = null) {
            val starter = Intent(context, WebActivity::class.java)
                .putExtra(PARAMS_URL, url)
                .putExtra(PARAMS_TITLE, title)
            context.startActivity(starter)
        }
    }


    private var mTitle: String? = ""
    private var mUrl: String? = ""

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var mWebView: NestedScrollAgentWebView

    override fun initView(savedInstanceState: Bundle?) {

        window.apply {
            isStatusBarTranslucentCompat = true
        }

        mUrl = intent.getStringExtra(PARAMS_URL)
        mTitle = intent.getStringExtra(PARAMS_TITLE)


        val webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                binding.toolbar.title = title.htmlEncode()
            }
        }


        val colorPrimary = MaterialColors.getColor(this, com.google.android.material.R.attr.colorPrimary, 0)

        mWebView = NestedScrollAgentWebView(this)

        val layoutParams = FrameLayout.LayoutParams(-1, -1)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.flWebContainer, layoutParams)
            .useDefaultIndicator(colorPrimary, 1)
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(WebClientFactory.create(mUrl))
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebView(mWebView)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) // 打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(mUrl)

        binding.apply {
            with(toolbar) {
                title = mTitle
                setNavigationOnClickListener {
                    clickBack()
                }
                post {
                    try {
                        val titleTextView = ToolbarUtils.getTitleTextView(toolbar)
                        titleTextView?.let {
                            it.ellipsize = TextUtils.TruncateAt.MARQUEE
                            it.isFocusable = true
                            it.isFocusableInTouchMode = true
                            it.requestFocus()
                        }
                        titleTextView?.ellipsize = TextUtils.TruncateAt.MARQUEE

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        addOnBackPressedCallback(true) {
            clickBack()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }


    private fun clickBack() {
        if (!mAgentWeb.back()) {
            finish()
        }
    }
}