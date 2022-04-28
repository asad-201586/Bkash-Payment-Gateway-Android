package ninja.saad.bkashdemo.features.bkash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ninja.saad.bkashdemo.R
import ninja.saad.bkashdemo.util.Config
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    val TAG = "main_act"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        getPassValue()
    }

    private fun getPassValue() {
        if (intent.extras != null) {
            val paymentId = intent.extras?.getString(Config.PAYMENT_ID)
            val transactionId = intent.extras?.getString(Config.TRANSACTION_ID)
            val amount = intent.extras?.getString(Config.AMOUNT)
            val paymentSerialize = intent.extras?.getString(Config.PAYMENT_SERIALIZE)

            Log.d(TAG, "getPassValue: paymentId: $paymentId")
            Log.d(TAG, "getPassValue: transactionId: $transactionId")
            Log.d(TAG, "getPassValue: amount: $amount")
            Log.d(TAG, "getPassValue: paymentSerialize: $paymentSerialize")

        }
        else Log.d(TAG, "getPassValue: pass value not found")
    }

    private fun initViews() {
        btn_pay_with_bkash.onClick {
            val intent = Intent(this@MainActivity, BkashPaymentActivity::class.java)
            intent.putExtra("amount", "37")
            //intent.putExtra("intent", "sale") //if you require Immediate transfer
            intent.putExtra("intent", "sale") // if you require Auth & Capture
            intent.putExtra("trx_id", "123456abcdef")
            startActivity(intent)
        }
    }
}
