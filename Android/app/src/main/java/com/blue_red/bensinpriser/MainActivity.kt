package com.blue_red.bensinpriser

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.blue_red.bensinpriser.api.ApiRetriever
import com.blue_red.bensinpriser.model.BDatum
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {


    private lateinit var refreshButton: Button

    private val api: ApiRetriever = ApiRetriever()

    private lateinit var list: List<BDatum>

    companion object {
        private const val TAG = "AppLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        onLoad()

        btnStartAnotherActivity.setOnClickListener {
            val intent = Intent(this, RecViewActivity::class.java)
            // start your next activity
            try {
                startActivity(intent)
            }
            catch (e: Exception)
            {
                println(e)
            }
        }
    }

    private fun onLoad() {
        if (isNetworkConnected()) {
            api.getData(callback)
        } else {

            Log.d(TAG, "---Show alert----")

            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    private fun setupUI() {
        refreshButton = findViewById(R.id.refreshButton)

        refreshButton.setOnClickListener {
            api.getData(callback)
        }
    }

    private val callback = object : Callback<List<BDatum>> {
        override fun onFailure(call: Call<List<BDatum>>?, t: Throwable?) {
            Log.e(TAG, "Problem calling API", t)
        }

        override fun onResponse(call: Call<List<BDatum>>?, response: Response<List<BDatum>>?) {

            val string = response?.body().toString()
            Log.e(TAG, string)

            response?.isSuccessful.let {
                if (response is Response<List<BDatum>>) {

                    list = response.body() ?: emptyList()
                    
//                    list.forEach{ Log.e(TAG, it.toString()) }
                }
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
