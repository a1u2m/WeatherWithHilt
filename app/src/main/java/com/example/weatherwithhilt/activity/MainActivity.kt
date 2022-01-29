package com.example.weatherwithhilt.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.weatherwithhilt.R
import com.example.weatherwithhilt.di.MyToast
import com.example.weatherwithhilt.model.OpenWeatherMapApi
import com.example.weatherwithhilt.model.Response
import com.squareup.picasso.Picasso
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val APP_ID = "511180"
    private val API_KEY = "###"
    private val LANG = "ru"
    private val UNITS = "metric"
    private val TAG = "MainActivity"

    lateinit var icon: AppCompatImageView
    lateinit var description: AppCompatTextView
    lateinit var temp: AppCompatTextView
    lateinit var feelsLike: AppCompatTextView
    lateinit var pressure: AppCompatTextView
    lateinit var humidity: AppCompatTextView
    lateinit var speed: AppCompatTextView
    lateinit var loading: AppCompatTextView
    lateinit var progressBar: ProgressBar

    @Inject
    lateinit var myToast: MyToast

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl("https://api.openweathermap.org")
        .build()

    private val api = retrofit.create(OpenWeatherMapApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        val flowable: Flowable<Response> = api.getResponse(APP_ID, API_KEY, LANG, UNITS)

        flowable.take(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSubscriber<Response>() {

                override fun onNext(t: Response?) {
                    Log.d(TAG, "onNext")

                    description.text =
                        "${description.text} ${t?.weather?.get(0)?.description?.capitalize()}"
                    temp.text = "${temp.text} ${t?.main?.temp}°C"
                    feelsLike.text = "${feelsLike.text} ${t?.main?.feelsLike}°C"
                    pressure.text = "${pressure.text} ${t?.main?.pressure} мм рт. ст."
                    humidity.text = "${humidity.text} ${t?.main?.humidity}%"
                    speed.text = "${speed.text} ${t?.wind?.speed} м/с"

                    val pictureLink =
                        "https://openweathermap.org/img/wn/${t?.weather?.get(0)?.icon}@2x.png"

                    Picasso.get()
                        .load(pictureLink)
                        .error(R.drawable.ic_weather_placeholder)
                        .into(icon)

                    loading.visibility = View.GONE
                    progressBar.visibility = View.GONE

                }

                override fun onError(t: Throwable?) {
                    myToast.show()
                    if (t != null) {
                        Log.d(TAG, "onError: ${t.message}")
                    } else {
                        Log.d(TAG, "onError: t == null")
                    }
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete")
                }
            })
    }

    private fun initViews() {
        icon = findViewById(R.id.icon)
        description = findViewById(R.id.description)
        temp = findViewById(R.id.temp)
        feelsLike = findViewById(R.id.feels_like)
        pressure = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        speed = findViewById(R.id.speed)
        icon.setImageResource(R.drawable.ic_weather_placeholder)
        loading = findViewById(R.id.loading)
        progressBar = findViewById(R.id.progress_bar)
    }
}