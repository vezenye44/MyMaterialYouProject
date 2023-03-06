package ru.geekbrains.mymaterialproject.ui.pictureoftheday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.mymaterialproject.BuildConfig
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayDTO
import ru.geekbrains.mymaterialproject.data.PictureOfTheDayData
import ru.geekbrains.mymaterialproject.data.retrofit.PictureOfTheDayRetrofit
import ru.geekbrains.mymaterialproject.util.date.*

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PictureOfTheDayRetrofit = PictureOfTheDayRetrofit()
) :
    ViewModel() {
    fun getData(): LiveData<PictureOfTheDayData> {
        return liveDataForViewToObserve
    }

    fun todayPictureOfTheDay() {
        val date = getCurrentDateTime(TODAY).toString("yyyy-MM-dd")
        sendServerRequest(date)
    }

    fun yesterdayPictureOfTheDay() {
        val date = getCurrentDateTime(YESTERDAY).toString("yyyy-MM-dd")
        sendServerRequest(date)
    }

    fun beforeYesterdayPictureOfTheDay() {
        val date = getCurrentDateTime(BEFORE_YESTERDAY).toString("yyyy-MM-dd")
        sendServerRequest(date)
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            (retrofitImpl as PictureOfTheDayRetrofit).getRetrofitImpl()
                .getPictureOfTheDayByDate(apiKey, date)
                .enqueue(object : Callback<PictureOfTheDayDTO> {
                    override fun onResponse(
                        call: Call<PictureOfTheDayDTO>,
                        response: Response<PictureOfTheDayDTO>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PictureOfTheDayDTO>, t: Throwable) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                    }
                })
        }
    }
}