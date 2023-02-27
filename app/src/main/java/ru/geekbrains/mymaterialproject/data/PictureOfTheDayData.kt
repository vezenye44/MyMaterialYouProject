package ru.geekbrains.mymaterialproject.data

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: PictureOfTheDayDTO) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}
