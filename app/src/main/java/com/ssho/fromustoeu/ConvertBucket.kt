package com.ssho.fromustoeu

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//todo не самое удачное форматирование, стоит использовать горячие клавиши или прекоммит хук
@Entity
data class ConvertBucket(
    @PrimaryKey val uid: Int,
    //todo надо использовать val вместо var. Дата-классы должны быть иммутабельными (immutable),
    // по-хорошему.
    var appTab: String,
    var sourceMeasureSystem: Int,
    var sourceUnitName: String,
    var targetUnitName: String,
    var sourceValueText: String
//todo зачем наследоваться от Serializable? вроде по дефолту всех возможностей дата-класса должно
// хватать
) : Serializable