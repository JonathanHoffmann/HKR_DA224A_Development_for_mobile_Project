package com.blue_red.bensinpriser.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blue_red.bensinpriser.R
import com.blue_red.bensinpriser.model.FuelStation
import com.bumptech.glide.Glide

class DetailViewActivity : AppCompatActivity() {

    private var fuelStation: FuelStation? = null

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView

    private lateinit var bensin98TextView: TextView
    private lateinit var bensin95TextView: TextView
    private lateinit var dieselTextView: TextView
    private lateinit var ethanol85TextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        setupUI()
        setupComponents()
    }


    private fun setupComponents() {
        imageView = findViewById(R.id.logoImageView)
        Glide.with(this).load(fuelStation?.imageUrl).into(imageView)
    }

    private fun setupUI() {
        val intent = this.intent
        val bundle = intent.extras
        fuelStation = bundle?.getSerializable("Station") as FuelStation?

        titleTextView = findViewById(R.id.TVtitle)
        titleTextView.text = fuelStation?.title

        subtitleTextView = findViewById(R.id.companyName)
        subtitleTextView.text = fuelStation?.getmCompanyName()

        bensin95TextView = findViewById(R.id.textView11)
        bensin95TextView.text = "Bensin95 price: " + formatForNull(fuelStation?.getmBensin95())

        bensin98TextView = findViewById(R.id.textView18)
        bensin98TextView.text = "Bensin98 price: " + formatForNull(fuelStation?.getmBensin98())

        dieselTextView = findViewById(R.id.textView12)
        dieselTextView.text = "Diesel price: " + fuelStation?.getmDiesel() + "SEK"

        ethanol85TextView = findViewById(R.id.textView13)
        ethanol85TextView.text = "Ethanol price: " + fuelStation?.getmEthanol85() + "SEK"
    }

    private fun formatForNull(price: Double?): String {
        if (price != null) {
            if (price == -1.0) {
                return "Not Available"
            }
            return price.toString() + " SEK"
        }
        return "Not Available"
    }
}