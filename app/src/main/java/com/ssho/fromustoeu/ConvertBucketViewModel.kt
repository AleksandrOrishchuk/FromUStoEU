package com.ssho.fromustoeu

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssho.fromustoeu.converters.Converters
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round

private const val TAG = "ConvertBucket"

class ConvertBucketViewModel {
    val bucketViewState: LiveData<ConvertBucketViewState> get() = _bucketViewState

    //todo не принципиально, но можно в некоторых местах не объявлять типы, тоже немного код
    // нагромождает))
    private val _bucketViewState: MutableLiveData<ConvertBucketViewState> = MutableLiveData()

    private lateinit var convertBucket: ConvertBucket

    //todo мне вот как раз не нравится эта архитектура тем, что она нарушает слои ответственности((
    // по идее, вьюмодель не должна ничего знать о вью, а тут бац, КликЛистенер висит..
    val onLongClickListener = object : View.OnLongClickListener {
        override fun onLongClick(itemView: View): Boolean {
            if (bucketViewState.value == null)
                return false

            //todo оператор (!!) лучше вообще не использовать.
            // Можно просто в конце дописать orEmpty(). В случае непредвиденного лучше пусть будет
            // пустая строка, чем креш)
            // val convertedValueText = bucketViewState.value?.convertedValueText.orEmpty()
            val convertedValueText = bucketViewState.value!!.convertedValueText
            val context = itemView.context
            copyTextToSystemClipboard(context, convertedValueText)
            showLongToast(context, R.string.copied_to_clipboard)

            return true
        }
    }

    //todo по кодстайлу лучше в самый верх поднимать инит, должна быть четкая структура
    // кода внутри класса
    init {
        _bucketViewState.value = ConvertBucketViewState()
    }

    // todo можно название сделать более говорящим, например, setConvertBucketAndUpdateViewState
    fun setConvertBucket(convertBucket: ConvertBucket) {
        this.convertBucket = convertBucket

        val convertedValue = getConvertResult()
        val convertedValueText = getValueText(convertedValue)

        updateViewState(
            _bucketViewState.value?.copy(
                convertedValueText = convertedValueText,
                sourceUnitName = convertBucket.sourceUnitName,
                targetUnitName = convertBucket.targetUnitName
            )
        )
    }

    private fun getValueText(convertedValue: Double): String {
        return if (floor(convertedValue) == convertedValue)
            convertedValue.toInt().toString()
        else
            String.format("%.2f", round(convertedValue * 100) / 100)
                .replace(',', '.')
    }

    private fun getConvertResult(): Double {
        var inputValue = convertBucket.sourceValueText.toDouble()
        val targetUnitName: String = convertBucket.targetUnitName

        //todo по-моему, вот это сравнение работать не будет, т.к. в названии единиц измерений
        // у тебя пишется с большой буквы: Celsius. Чтобы сработало,
        // надо сравнивать строки через метод equals, передавая параметр ignoreCase = true.
        // А вообще это надо вынести в константы и не писать тут руками названия юнитов,
        // а брать их из того же источника, где ты определил для UI.
        // Ну и еще если ты сделаешь интернационализацию, это тоже работать не будет))))))))))
        if (targetUnitName != "celsius" && targetUnitName != "fahrenheits")
            inputValue = abs(inputValue)

        val result = Converters.convert(inputValue, targetUnitName)
        Log.i(TAG, "Got converted value to $targetUnitName = $result")

        return result
    }

    private fun updateViewState(newViewState: ConvertBucketViewState?) {
        _bucketViewState.value = newViewState
    }
}

//todo должны быть val вместо var
data class ConvertBucketViewState(
    var sourceUnitName: String = "",
    var convertedValueText: String = "",
    var targetUnitName: String = ""
)
