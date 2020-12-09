package com.ssho.fromustoeu

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ConvertBucket(@PrimaryKey val uid: Int,
                         var appTab: String,
                         var measureSystemFrom: Int,
                         var measureTypeFrom: String,
                         var measureTypeTo: String
                         ) : Serializable