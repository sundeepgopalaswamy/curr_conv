package com.sundeep.demo.currconv.data.datasource

import kotlinx.coroutines.delay
import kotlin.random.Random

class RemoteDataSource {
    suspend fun getCurrencies(): List<List<String>> {
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
        )
    }

    suspend fun getConversions(): List<List<String>> {
        delay(3_000)
        val deciRandom = Random.nextDouble(-0.25, 0.25)
        val deci1Random = Random.nextDouble(-0.025, 0.025)
        val deci2Random = Random.nextDouble(-0.0025, 0.0025)
        val deci3Random = Random.nextDouble(-0.00025, 0.00025)
        val deci4Random = Random.nextDouble(-0.000025, 0.000025)
        return listOf(
            listOf("USD", "INR", (84.000 + deciRandom).toString()),
            listOf("USD", "JPY", (142.330 + deciRandom).toString()),
            listOf("GBP", "JPY", (185.185 + deciRandom).toString()),
            listOf("AUD", "GBP", (0.51 + deci2Random).toString()),
            listOf("USD", "EUR", (0.90 + deci2Random).toString()),
            listOf("USD", "AED", (3.67 + deci1Random).toString()),

            listOf("USD", "AUD", (1.5 + deci1Random).toString()),
            listOf("USD", "GBP", (0.76 + deci2Random).toString()),
            listOf("INR", "JPY", (1.68 + deci1Random).toString()),
            listOf("INR", "GBP", (0.0091 + deci4Random).toString()),
            listOf("INR", "AUD", (0.018 + deci3Random).toString()),
            listOf("INR", "EUR", (0.011 + deci3Random).toString()),
            listOf("INR", "AED", (0.044 + deci3Random).toString()),
            listOf("JPY", "AUD", (0.11 + deci2Random).toString()),
            listOf("JPY", "EUR", (0.0064 + deci4Random).toString()),
            listOf("JPY", "AED", (0.026 + deci3Random).toString()),
            listOf("GBP", "EUR", (1.19 + deci1Random).toString()),
            listOf("GBP", "AED", (4.81 + deci1Random).toString()),
            listOf("AUD", "EUR", (0.6 + deci2Random).toString()),
            listOf("AUD", "AED", (2.45 + deci1Random).toString()),
            listOf("EUR", "AED", (4.06 + deci1Random).toString()),
        )
    }

}