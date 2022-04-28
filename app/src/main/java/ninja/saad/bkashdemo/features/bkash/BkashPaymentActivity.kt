package ninja.saad.bkashdemo.features.bkash

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bkash_payment.*
import ninja.saad.bkashdemo.R
import ninja.saad.bkashdemo.data.BkashPaymentRequest
import ninja.saad.bkashdemo.util.JSInterface
import org.json.JSONObject

class BkashPaymentActivity : AppCompatActivity() {

    private var paymentRequest = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bkash_payment)

        val request = BkashPaymentRequest(intent.getStringExtra("amount"), intent.getStringExtra("intent"))
        val trxId = intent.getStringExtra("trx_id")
        //paymentRequest = "{paymentRequest:${Gson().toJson(request)}}"
        paymentRequest = Gson().toJson(request)


        initBkashWebView()
        initBkashWebViewClient(getJsonObject(request, trxId))
    }

    private fun getJsonObject(
        request: BkashPaymentRequest,
        trxId: String?
    ): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("amount", request.amount)
        jsonObject.put("intent", request.intent)
        jsonObject.put("trx_id", trxId)
        return jsonObject
    }

    private fun initBkashWebView() {
        bkashWebView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }

        bkashWebView.apply {
            clearCache(true)
            isFocusableInTouchMode = true
            addJavascriptInterface(JSInterface(this@BkashPaymentActivity), "OsudPotro")
            //loadUrl("file:///android_asset/www/checkout_120.html")
            loadUrl("http://192.168.81.15:8080/pgw-merchant-backend-php-master/php/payment.php")

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.d("main_act", "onConsoleMessage: webViewConsole: ${consoleMessage?.message()}, line_number: ${consoleMessage?.lineNumber()}")
                    return super.onConsoleMessage(consoleMessage)
                }
            }
        }
    }

    private fun initBkashWebViewClient(paymentRequestObject: JSONObject) {
        bkashWebView.webViewClient = object : WebViewClient(){

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                loadingProgressBar.visibility = VISIBLE
                if (url == "https://www.bkash.com/terms-and-conditions") {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loadingProgressBar.visibility = VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("main_act", "onPageFinished: my_json: $paymentRequestObject")
                bkashWebView.let {
                    it.loadUrl("javascript:callReconfigure($paymentRequestObject)")
                    it.loadUrl("javascript:clickPayButton()")
                }
                loadingProgressBar.visibility = GONE
            }
        }
    }

}