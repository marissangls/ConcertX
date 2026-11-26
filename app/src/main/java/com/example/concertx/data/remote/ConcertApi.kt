package com.example.concertx.data.remote

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.concertx.data.local.Concert
import com.example.concertx.data.local.DatabaseHelper

object ConcertApi {

    private const val URL =
        "https://gist.githubusercontent.com/axelkrnwn/31139c9cfee4aca4acee3a36907a8c7e/raw/f4be1d6783d4331363e92b1bc450bf77259ea07c/concertx.json"

    fun fetchAndSave(context: Context, onDone: () -> Unit) {
        val db = DatabaseHelper(context)

        val request = JsonArrayRequest(Request.Method.GET, URL, null,
            { response ->
                for (i in 0 until response.length()) {
                    val o = response.getJSONObject(i)
                    db.insertConcert(
                        Concert(
                            id = o.getInt("concertId"),
                            title = o.getString("title"),
                            artist = o.getString("artist"),
                            venue = o.getString("venue"),
                            city = o.getString("city"),
                            latitude = o.getDouble("latitude"),
                            longitude = o.getDouble("longitude"),
                            date = o.getString("date"),
                            price = o.getInt("price"),
                            image = o.optString("image"),
                            description = o.optString("description"),
                            stock = o.getInt("stock")
                        )
                    )
                }
                onDone()
            },
            {
                Toast.makeText(context, "Failed to load concerts", Toast.LENGTH_SHORT).show()
                onDone()
            }
        )

        Volley.newRequestQueue(context.applicationContext).add(request)
    }
}