package ninja.saad.bkashdemo.util

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import ninja.saad.bkashdemo.features.bkash.MainActivity
import android.content.DialogInterface
import android.util.Log

import android.widget.Toast
import ninja.saad.bkashdemo.util.Config.AMOUNT
import ninja.saad.bkashdemo.util.Config.PAYMENT_ID
import ninja.saad.bkashdemo.util.Config.PAYMENT_SERIALIZE
import ninja.saad.bkashdemo.util.Config.TRANSACTION_ID


class JSInterface(var context: Context) {

    @JavascriptInterface
    fun onPaymentSuccess(data: String) {
        Log.d("main_act", "onPaymentSuccess: javaScriptInterface called: $data")
        val dataArray = data.split("&")
        val paymentId = dataArray[0].trim().replace("PaymentID= ","").trim()
        val transactionId = dataArray[1].trim().replace("TransactionID= ","").trim()
        val amount = dataArray[2].trim().replace("Amount= ","").trim()

        //payment success
        val intent = Intent(context,MainActivity::class.java)
        intent.putExtra(PAYMENT_ID,paymentId)
        intent.putExtra(TRANSACTION_ID,transactionId)
        intent.putExtra(AMOUNT,amount)
        intent.putExtra(PAYMENT_SERIALIZE,data)
        context.startActivity(intent)
    }
//    fun switchActivity() {
//        val intent = Intent(context, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        context.startActivity(intent)
//
//
////        val tranArray: Array<String> = getTransectionId.split("&")
////        val finalTran = tranArray[1].split("=".toRegex()).toTypedArray()
////        Toast.makeText(context, "Success Payment", Toast.LENGTH_SHORT).show()
////
////        AlertDialog.Builder(context)
////            .setIcon(R.drawable.ic_dialog_alert)
////            .setCancelable(false)
////            .setTitle("Order Successful")
////            .setMessage("Your Order Has Been Confirmed with Payment")
////            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
////                val i = Intent(context, MainActivity::class.java)
////                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
////                context.startActivity(i)
////            })
////            .show()
//
//    }
}