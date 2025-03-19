package com.sundeep.demo.currconv.data.datasource

import kotlinx.coroutines.delay
import kotlin.random.Random

class SimulatedRemoteDataSource : RemoteDataSource {
    override suspend fun getCurrencies(): List<List<String>> {
        delay(3_000)
        return listOf(
            listOf(
                "Indian Rupee",
                "INR",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/india.png"
            ),
            listOf(
                "Japanese Yen",
                "JPY",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/japan.png"
            ),
            listOf(
                "British Pound",
                "GBP",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/uk.jpg"
            ),
            listOf(
                "Australian Dollar",
                "AUD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/aus.png"
            ),
            listOf(
                "Euro",
                "EUR",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/euro.png"
            ),
            listOf(
                "Emirati Dirham",
                "AED",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/uae.png"
            ),
            listOf(
                "US Dollar",
                "USD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/usa.jpg"
            ),
            listOf(
                "Canadian Dollar",
                "CAD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/canada.png"
            ),
            listOf(
                "Chinese Yuan",
                "CNY",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/china.png"
            ),
            listOf(
                "Brazilian Real",
                "BRL",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/brazil.png"
            ),
            listOf(
                "Swiss Franc",
                "CHF",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/switzerland.png"
            ),
            listOf(
                "Hong Kong Dollar",
                "HKD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/hongkong.png"
            ),
            listOf(
                "South Korean Won",
                "KRW",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/southkorea.png"
            ),
            listOf(
                "Mexican Peso",
                "MXN",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/mexico.png"
            ),
            listOf(
                "Norwegian Krone",
                "NOK",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/norway.png"
            ),
            listOf(
                "New Zealand Dollar",
                "NZD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/newzealand.png"
            ),
            listOf(
                "Russian Ruble",
                "RUB",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/russia.png"
            ),
            listOf(
                "Swedish Krona",
                "SEK",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/sweden.png"
            ),
            listOf(
                "Singapore Dollar",
                "SGD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/singapore.png"
            ),
            listOf(
                "South African Rand",
                "ZAR",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/southafrica.png"
            ),
            listOf(
                "Israeli New Shekel",
                "ILS",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/israel.png"
            ),
            listOf(
                "Thai Baht",
                "THB",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/thailand.png"
            ),
        )
    }

    override suspend fun getConversions(): List<List<String>> {
        delay(3_000)
        val deciRandom = Random.nextDouble(-0.25, 0.25)
        val deci1Random = Random.nextDouble(-0.025, 0.025)
        val deci2Random = Random.nextDouble(-0.0025, 0.0025)
        val deci3Random = Random.nextDouble(-0.00025, 0.00025)
        val deci4Random = Random.nextDouble(-0.000025, 0.000025)
        return listOf(
            listOf("USD", "INR", (84.000 + deciRandom).toString()),
            listOf("USD", "JPY", (142.330 + deciRandom).toString()),
            listOf("USD", "EUR", (0.90 + deci2Random).toString()),
            listOf("USD", "AED", (3.67 + deci1Random).toString()),
            listOf("USD", "AUD", (1.5 + deci1Random).toString()),
            listOf("USD", "GBP", (0.76 + deci2Random).toString()),
            listOf("USD", "CNY", (7.30848 + deciRandom).toString()),
            listOf("USD", "BRL", (5.765051 + deciRandom).toString()),
            listOf("USD", "CAD", (1.433654 + deciRandom).toString()),
            listOf("USD", "CHF", (0.91036 + deci2Random).toString()),
            listOf("USD", "HKD", (7.790837 + deciRandom).toString()),
            listOf("USD", "KRW", (1452.766696 + deciRandom).toString()),
            listOf("USD", "MXN", (20.622966 + deciRandom).toString()),
            listOf("USD", "NOK", (11.266893 + deciRandom).toString()),
            listOf("USD", "NZD", (1.769635 + deci1Random).toString()),
            listOf("USD", "RUB", (97.0239 + deciRandom).toString()),
            listOf("USD", "SEK", (10.972114 + deciRandom).toString()),
            listOf("USD", "SGD", (1.355113 + deci1Random).toString()),
            listOf("USD", "ZAR", (18.503631 + deciRandom).toString()),
            listOf("USD", "ILS", (3.668427 + deci1Random).toString()),
            listOf("USD", "THB", (33.6105 + deciRandom).toString()),

            listOf("CNY", "AED", (0.502499 + deci2Random).toString()),
            listOf("CNY", "AUD", (0.218548 + deci2Random).toString()),
            listOf("CNY", "BRL", (0.793504 + deci2Random).toString()),
            listOf("CNY", "CAD", (0.196264 + deci2Random).toString()),
            listOf("CNY", "CHF", (0.124486 + deci2Random).toString()),
            listOf("CNY", "EUR", (0.132592 + deci2Random).toString()),
            listOf("CNY", "GBP", (0.11036 + deci2Random).toString()),
            listOf("CNY", "HKD", (1.068752 + deci1Random).toString()),
            listOf("CNY", "INR", (11.991826 + deciRandom).toString()),
            listOf("CNY", "JPY", (20.784304 + deciRandom).toString()),
            listOf("CNY", "KRW", (199.003632 + deciRandom).toString()),
            listOf("CNY", "MXN", (2.823166 + deci1Random).toString()),
            listOf("CNY", "NOK", (1.542392 + deci1Random).toString()),
            listOf("CNY", "NZD", (0.241865 + deci2Random).toString()),
            listOf("CNY", "RUB", (13.281035 + deciRandom).toString()),
            listOf("CNY", "SEK", (1.499956 + deci1Random).toString()),
            listOf("CNY", "SGD", (0.18567 + deci2Random).toString()),
            listOf("CNY", "ZAR", (2.539223 + deci1Random).toString()),
            listOf("CNY", "ILS", (0.507568 + deci2Random).toString()),
            listOf("CNY", "THB", (4.6490 + deci1Random).toString()),

            listOf("INR", "JPY", (1.68 + deci1Random).toString()),
            listOf("INR", "GBP", (0.0091 + deci4Random).toString()),
            listOf("INR", "AUD", (0.018 + deci3Random).toString()),
            listOf("INR", "EUR", (0.011 + deci3Random).toString()),
            listOf("INR", "AED", (0.044 + deci3Random).toString()),
            listOf("INR", "BRL", (0.066005 + deci3Random).toString()),
            listOf("INR", "CAD", (0.016357 + deci3Random).toString()),
            listOf("INR", "CHF", (0.010383 + deci3Random).toString()),
            listOf("INR", "HKD", (0.088922 + deci3Random).toString()),
            listOf("INR", "KRW", (16.535146 + deciRandom).toString()),
            listOf("INR", "MXN", (0.234582 + deci2Random).toString()),
            listOf("INR", "NOK", (0.128198 + deci2Random).toString()),
            listOf("INR", "NZD", (0.020149 + deci3Random).toString()),
            listOf("INR", "RUB", (1.107347 + deci1Random).toString()),
            listOf("INR", "SEK", (0.124725 + deci2Random).toString()),
            listOf("INR", "SGD", (0.015467 + deci3Random).toString()),
            listOf("INR", "ZAR", (0.210452 + deci2Random).toString()),
            listOf("INR", "ILS", (0.042465 + deci3Random).toString()),
            listOf("INR", "THB", (0.38795 + deci2Random).toString()),

            listOf("GBP", "JPY", (185.185 + deciRandom).toString()),
            listOf("GBP", "EUR", (1.19 + deci1Random).toString()),
            listOf("GBP", "AED", (4.81 + deci1Random).toString()),
            listOf("GBP", "BRL", (7.164549 + deci1Random).toString()),
            listOf("GBP", "CAD", (1.773777 + deci1Random).toString()),
            listOf("GBP", "CHF", (1.127329 + deci1Random).toString()),
            listOf("GBP", "HKD", (9.643274 + deci1Random).toString()),
            listOf("GBP", "KRW", (1796.87197 + deciRandom).toString()),
            listOf("GBP", "MXN", (25.500831 + deciRandom).toString()),
            listOf("GBP", "NOK", (13.87577 + deciRandom).toString()),
            listOf("GBP", "NZD", (2.192918 + deci1Random).toString()),
            listOf("GBP", "RUB", (119.951982 + deciRandom).toString()),
            listOf("GBP", "SEK", (13.521148 + deciRandom).toString()),
            listOf("GBP", "SGD", (1.677609 + deci1Random).toString()),
            listOf("GBP", "ZAR", (22.808689 + deciRandom).toString()),
            listOf("GBP", "ILS", (4.7701 + deci1Random).toString()),
            listOf("GBP", "THB", (43.687 + deciRandom).toString()),

            listOf("AUD", "AED", (2.45 + deci1Random).toString()),
            listOf("AUD", "BRL", (3.631446 + deci1Random).toString()),
            listOf("AUD", "CAD", (0.899047 + deci2Random).toString()),
            listOf("AUD", "CHF", (0.571633 + deci2Random).toString()),
            listOf("AUD", "EUR", (0.6 + deci2Random).toString()),
            listOf("AUD", "GBP", (0.51 + deci2Random).toString()),
            listOf("AUD", "HKD", (4.886813 + deci1Random).toString()),
            listOf("AUD", "KRW", (910.031244 + deciRandom).toString()),
            listOf("AUD", "MXN", (12.933451 + deciRandom).toString()),
            listOf("AUD", "NOK", (7.038736 + deci1Random).toString()),
            listOf("AUD", "NZD", (1.111262 + deci1Random).toString()),
            listOf("AUD", "RUB", (60.803206 + deciRandom).toString()),
            listOf("AUD", "SEK", (6.855348 + deci1Random).toString()),
            listOf("AUD", "SGD", (0.850098 + deci2Random).toString()),
            listOf("AUD", "ZAR", (11.56614 + deciRandom).toString()),
            listOf("AUD", "ILS", (2.33348 + deci1Random).toString()),
            listOf("AUD", "THB", (21.3738 + deciRandom).toString()),

            listOf("JPY", "AED", (0.026 + deci3Random).toString()),
            listOf("JPY", "AUD", (0.11 + deci2Random).toString()),
            listOf("JPY", "BRL", (0.038083 + deci3Random).toString()),
            listOf("JPY", "CAD", (0.009436 + deci4Random).toString()),
            listOf("JPY", "CHF", (0.005999 + deci4Random).toString()),
            listOf("JPY", "EUR", (0.0064 + deci4Random).toString()),
            listOf("JPY", "HKD", (0.051285 + deci3Random).toString()),
            listOf("JPY", "KRW", (9.543444 + deci1Random).toString()),
            listOf("JPY", "MXN", (0.1357 + deci2Random).toString()),
            listOf("JPY", "NOK", (0.073834 + deci3Random).toString()),
            listOf("JPY", "NZD", (0.011667 + deci3Random).toString()),
            listOf("JPY", "RUB", (0.637776 + deci2Random).toString()),
            listOf("JPY", "SEK", (0.071925 + deci3Random).toString()),
            listOf("JPY", "SGD", (0.008923 + deci4Random).toString()),
            listOf("JPY", "ZAR", (0.121375 + deci2Random).toString()),
            listOf("JPY", "ILS", (0.024609 + deci3Random).toString()),
            listOf("JPY", "THB", (0.2251 + deci2Random).toString()),

            listOf("EUR", "AED", (4.06 + deci1Random).toString()),
            listOf("EUR", "BRL", (5.965051 + deci1Random).toString()),
            listOf("EUR", "CAD", (1.473654 + deci1Random).toString()),
            listOf("EUR", "CHF", (0.939 + deci2Random).toString()),
            listOf("EUR", "HKD", (8.031558 + deci1Random).toString()),
            listOf("EUR", "KRW", (1496.097538 + deciRandom).toString()),
            listOf("EUR", "MXN", (21.248642 + deciRandom).toString()),
            listOf("EUR", "NOK", (11.555987 + deciRandom).toString()),
            listOf("EUR", "NZD", (1.8262 + deci1Random).toString()),
            listOf("EUR", "RUB", (99.9259 + deciRandom).toString()),
            listOf("EUR", "SEK", (11.264 + deciRandom).toString()),
            listOf("EUR", "SGD", (1.39736 + deci1Random).toString()),
            listOf("EUR", "ZAR", (19.0067 + deciRandom).toString()),
            listOf("EUR", "ILS", (4.008216 + deci1Random).toString()),
            listOf("EUR", "THB", (36.774 + deciRandom).toString()),

            listOf("AED", "BRL", (1.5755 + deci1Random).toString()),
            listOf("AED", "CAD", (0.3901 + deci2Random).toString()),
            listOf("AED", "CHF", (0.248 + deci2Random).toString()),
            listOf("AED", "HKD", (2.121 + deci1Random).toString()),
            listOf("AED", "KRW", (394.936569 + deciRandom).toString()),
            listOf("AED", "MXN", (5.61303 + deci1Random).toString()),
            listOf("AED", "NOK", (3.053576 + deci1Random).toString()),
            listOf("AED", "NZD", (0.482294 + deci2Random).toString()),
            listOf("AED", "RUB", (26.383235 + deciRandom).toString()),
            listOf("AED", "SEK", (2.975042 + deci1Random).toString()),
            listOf("AED", "SGD", (0.368936 + deci2Random).toString()),
            listOf("AED", "ZAR", (5.020069 + deci1Random).toString()),
            listOf("AED", "ILS", (1.00056 + deci1Random).toString()),
            listOf("AED", "THB", (9.1478 + deci1Random).toString()),

            listOf("BRL", "CAD", (0.247484 + deci2Random).toString()),
            listOf("BRL", "CHF", (0.157397 + deci2Random).toString()),
            listOf("BRL", "HKD", (1.344678 + deci1Random).toString()),
            listOf("BRL", "KRW", (250.84166 + deciRandom).toString()),
            listOf("BRL", "MXN", (3.564405 + deci1Random).toString()),
            listOf("BRL", "NOK", (1.938608 + deci1Random).toString()),
            listOf("BRL", "NZD", (0.305297 + deci2Random).toString()),
            listOf("BRL", "RUB", (16.736271 + deciRandom).toString()),
            listOf("BRL", "SEK", (1.888224 + deci1Random).toString()),
            listOf("BRL", "SGD", (0.234128 + deci2Random).toString()),
            listOf("BRL", "ZAR", (3.186579 + deci1Random).toString()),
            listOf("BRL", "ILS", (0.644128 + deci2Random).toString()),
            listOf("BRL", "THB", (5.92 + deci1Random).toString()),

            listOf("CAD", "CHF", (0.635647 + deci2Random).toString()),
            listOf("CAD", "HKD", (5.43524 + deci1Random).toString()),
            listOf("CAD", "KRW", (1012.240429 + deciRandom).toString()),
            listOf("CAD", "MXN", (14.383456 + deciRandom).toString()),
            listOf("CAD", "NOK", (7.825984 + deci1Random).toString()),
            listOf("CAD", "NZD", (1.235516 + deci1Random).toString()),
            listOf("CAD", "RUB", (67.602492 + deciRandom).toString()),
            listOf("CAD", "SEK", (7.624189 + deci1Random).toString()),
            listOf("CAD", "SGD", (0.94562 + deci2Random).toString()),
            listOf("CAD", "ZAR", (12.868825 + deciRandom).toString()),
            listOf("CAD", "ILS", (2.563516 + deci1Random).toString()),
            listOf("CAD", "THB", (23.483631 + deciRandom).toString()),

            listOf("CHF", "HKD", (8.5514 + deci1Random).toString()),
            listOf("CHF", "KRW", (1593.117381 + deciRandom).toString()),
            listOf("CHF", "MXN", (22.62584 + deciRandom).toString()),
            listOf("CHF", "NOK", (12.311043 + deciRandom).toString()),
            listOf("CHF", "NZD", (1.938608 + deci1Random).toString()),
            listOf("CHF", "RUB", (106.378456 + deciRandom).toString()),
            listOf("CHF", "SEK", (12.121148 + deciRandom).toString()),
            listOf("CHF", "SGD", (1.4875 + deci1Random).toString()),
            listOf("CHF", "ZAR", (20.234 + deciRandom).toString()),
            listOf("CHF", "ILS", (4.185756 + deci1Random).toString()),
            listOf("CHF", "THB", (38.3182 + deciRandom).toString()),

            listOf("HKD", "KRW", (186.248283 + deciRandom).toString()),
            listOf("HKD", "MXN", (2.646426 + deci1Random).toString()),
            listOf("HKD", "NOK", (1.439961 + deci1Random).toString()),
            listOf("HKD", "NZD", (0.227331 + deci2Random).toString()),
            listOf("HKD", "RUB", (12.438781 + deciRandom).toString()),
            listOf("HKD", "SEK", (1.402529 + deci1Random).toString()),
            listOf("HKD", "SGD", (0.18567 + deci2Random).toString()),
            listOf("HKD", "ZAR", (2.366933 + deci1Random).toString()),
            listOf("HKD", "ILS", (0.47567 + deci2Random).toString()),
            listOf("HKD", "THB", (2126.631 + deciRandom).toString()),

            listOf("KRW", "MXN", (0.014199 + deci3Random).toString()),
            listOf("KRW", "NOK", (0.007732 + deci4Random).toString()),
            listOf("KRW", "NZD", (0.001221 + deci4Random).toString()),
            listOf("KRW", "RUB", (0.066773 + deci3Random).toString()),
            listOf("KRW", "SEK", (0.007529 + deci4Random).toString()),
            listOf("KRW", "ZAR", (0.012705 + deci3Random).toString()),
            listOf("KRW", "ILS", (0.002529 + deci4Random).toString()),
            listOf("KRW", "THB", (0.02317 + deci3Random).toString()),

            listOf("MXN", "NOK", (0.544021 + deci2Random).toString()),
            listOf("MXN", "NZD", (0.085972 + deci3Random).toString()),
            listOf("MXN", "RUB", (4.699444 + deci1Random).toString()),
            listOf("MXN", "SEK", (0.529884 + deci2Random).toString()),
            listOf("MXN", "SGD", (0.065751 + deci3Random).toString()),
            listOf("MXN", "ZAR", (0.894088 + deci2Random).toString()),
            listOf("MXN", "ILS", (0.18567 + deci2Random).toString()),
            listOf("MXN", "THB", (1.68655 + deci1Random).toString()),

            listOf("NOK", "NZD", (0.157898 + deci2Random).toString()),
            listOf("NOK", "RUB", (8.63749 + deci1Random).toString()),
            listOf("NOK", "SEK", (0.974311 + deci2Random).toString()),
            listOf("NOK", "SGD", (0.120841 + deci2Random).toString()),
            listOf("NOK", "ZAR", (1.643649 + deci1Random).toString()),
            listOf("NOK", "ILS", (0.120841 + deci2Random).toString()),
            listOf("NOK", "THB", (3.1814 + deci1Random).toString()),

            listOf("NZD", "RUB", (54.711799 + deciRandom).toString()),
            listOf("NZD", "SEK", (6.167759 + deci1Random).toString()),
            listOf("NZD", "SGD", (0.765326 + deci2Random).toString()),
            listOf("NZD", "ZAR", (10.403213 + deciRandom).toString()),
            listOf("NZD", "ILS", (2.13319 + deci1Random).toString()),
            listOf("NZD", "THB", (19.5502 + deciRandom).toString()),

            listOf("RUB", "SEK", (0.112772 + deci2Random).toString()),
            listOf("RUB", "SGD", (0.013985 + deci3Random).toString()),
            listOf("RUB", "ZAR", (0.190261 + deci2Random).toString()),
            listOf("RUB", "ILS", (0.045985 + deci3Random).toString()),
            listOf("RUB", "THB", (0.400828 + deci2Random).toString()),

            listOf("SEK", "SGD", (0.124024 + deci2Random).toString()),
            listOf("SEK", "ZAR", (1.68733 + deci1Random).toString()),
            listOf("SEK", "ILS", (0.36564 + deci2Random).toString()),
            listOf("SEK", "THB", (3.3464 + deci1Random).toString()),

            listOf("SGD", "KRW", (1070.884883 + deciRandom).toString()),
            listOf("SGD", "ZAR", (13.600272 + deciRandom).toString()),
            listOf("SGD", "ILS", (2.76319 + deci1Random).toString()),
            listOf("SGD", "THB", (25.26234 + deciRandom).toString()),

            listOf("ZAR", "ILS", (0.204024 + deci2Random).toString()),
            listOf("ZAR", "THB", (1.85567 + deci1Random).toString()),

            listOf("ILS", "THB", (9.164252 + deci1Random).toString()),
        )
    }

}