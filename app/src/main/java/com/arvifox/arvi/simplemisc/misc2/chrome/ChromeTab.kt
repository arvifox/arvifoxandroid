package com.arvifox.arvi.simplemisc.misc2.chrome

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.*
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent

object sfs {
    val b = CustomTabsIntent.Builder()
}

object webwe {

    @SuppressLint("SetJavaScriptEnabled")
    fun f(webView: WebView) {
        webView.loadUrl("https://android-tools.ru")
        webView.loadUrl("file:///Android_res/raw/some_file.HTML")
        webView.loadUrl("file:///Android_asset/some_file.HTML")
        val rawHTML = "<HTML>" + "<body><h1>HTML content</h1></body>" + "</HTML>"
        webView.loadData(rawHTML, "text/HTML", "UTF-8")

        val s = webView.settings
        s.javaScriptEnabled = true

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.addJavascriptInterface(JavaScriptInterface(webView.context), "asd")

        webView.loadUrl("javascript:increment()")
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {}

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }
    }

    private class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return true
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return true
        }

        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
            return true
        }
    }

    class JavaScriptInterface internal constructor(c: Context) {
        var mContext: Context = c
        @JavascriptInterface
        fun showToast(toast: String?) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

    /*

    <html>
  <head>
    <meta charset="UTF-8">
  </head>
  <body>
    <center><b><u>Javascript</u></b></center>
    <div id="msg">0</div>
    <script>
      function increment() {
      var ob = document.getElementById("msg");
      ob.innerText = parseInt(ob.innerText) + 1;
      }
    </script>
  </body>
</html>

     */
}