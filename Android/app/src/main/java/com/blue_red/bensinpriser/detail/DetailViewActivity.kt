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
        val titleText = findViewById<TextView>(R.id.TVtitle)
        titleText.text = fuelStation?.title

        val subtitleText = findViewById<TextView>(R.id.companyName)
        subtitleText.text = fuelStation?.getmCompanyName()
    }
}