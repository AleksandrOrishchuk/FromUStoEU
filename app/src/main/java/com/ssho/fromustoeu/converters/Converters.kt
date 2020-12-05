package com.ssho.fromustoeu.converters

class Converters {
    companion object {
        fun convert(value: Double, convertToType: String): Double {
            value.let {
                return when (convertToType) {
                    "celsius" -> toCelsius(it)
                    "fahrenheits" -> toFahrenheits(it)
                    "inches" -> toInch(it)
                    "feet" -> toFeet(it)
                    "meters" -> toMeters(it)
                    "square Feet" -> toSqFeet(it)
                    "square Meters" -> toSqMeters(it)
                    "miles Per Hour" -> toMPH(it)
                    "kilometers Per Hour" -> toKMPH(it)
                    "kilograms" -> toKilograms(it)
                    "pounds" -> toPounds(it)
                    else -> it
                }
            }
        }

        //input type is Kilograms
        private fun toPounds(input: Double): Double {
            return input * 2.205
        }

        //input type is Pounds
        private fun toKilograms(input: Double): Double {
            return input / 2.205
        }

        private fun toKMPH(input: Double): Double {
            return input * 1.609
        }

        private fun toMPH(input: Double): Double {
            return input / 1.609
        }

        private fun toMeters(input: Double): Double {
            return input / 3.281
        }

        private fun toSqMeters(input: Double): Double {
            return input / 10.764
        }

        private fun toFahrenheits(input: Double): Double {
            return input * 1.8 + 32
        }

        private fun toSqFeet(input: Double): Double {
            return input * 10.764
        }

        private fun toFeet(input: Double): Double {
            return input * 3.281
        }

        private fun toInch(input: Double): Double {
            return input * 39.37
        }

        private fun toCelsius(input: Double): Double {
            return (input - 32) / 1.8
        }
    }
}