package com.ssho.fromustoeu.converters

class Converters {
    companion object {
        fun convert(value: Double, convertTargetName: String): Double {
            value.let {
                return when (convertTargetName) {
                    "celsius" -> toCelsius(it)
                    "fahrenheits" -> toFahrenheits(it)
                    "meters" -> toMeters(it)
                    "feet" -> toFeet(it)
                    "square_meters" -> toSqMeters(it)
                    "square_feet" -> toSqFeet(it)
                    "kilometers_per_hour" -> toKMPH(it)
                    "miles_per_hour" -> toMPH(it)
                    "kilograms" -> toKilograms(it)
                    "pounds" -> toPounds(it)
                    "inches" -> toInch(it)
                    "centimeters" -> toCentimeters(it)
                    "litres" -> toLitres(it)
                    "gallons" -> toGallons(it)
                    "grams" -> toGrams(it)
                    "ounces" -> toOunces(it)
                    else -> it
                }
            }
        }

        private fun toOunces(input: Double): Double {
            return input / 28.35
        }

        private fun toGrams(input: Double): Double {
            return input * 28.35
        }

        private fun toGallons(input: Double): Double {
            return input / 3.785
        }

        private fun toLitres(input: Double): Double {
            return input * 3.785
        }

        private fun toCentimeters(input: Double): Double {
            return input * 2.54
        }

        private fun toInch(input: Double): Double {
            return input / 2.54
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

        private fun toSqFeet(input: Double): Double {
            return input * 10.764
        }

        private fun toFeet(input: Double): Double {
            return input * 3.281
        }

        private fun toFahrenheits(input: Double): Double {
            return input * 1.8 + 32
        }

        private fun toCelsius(input: Double): Double {
            return (input - 32) / 1.8
        }
    }
}