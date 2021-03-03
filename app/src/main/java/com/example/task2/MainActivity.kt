package com.example.task2

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toColorLong
import com.example.task2.data.PeopleApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var listOfItems:MutableList<ShortData>
    lateinit var listOfItems2:MutableList<ShortData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = PeopleApiService()

        setContentView(R.layout.activity_main)
        GlobalScope.launch (Dispatchers.Main){
            val posts = apiService.getData(5).await()
            tv1.text=posts.results[0].location.country
            for (i in posts.results)
            {
                listOfItems.add(ShortData(i.name.first,i.name.last,i.location.country,i.location.city,i.location.street,i.email,i.login.username,i.login.password,i.dob,i.phone,i.picture.large,i.nat))
            }
        }


    }


    fun onClick(view: View?) {
        val db = baseContext.openOrCreateDatabase("app.db", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users (Firstname TEXT, LastName TEXT, Country TEXT, City TEXT, Street TEXT, Email TEXT, Username TEXT, Password TEXT, BithdayDate TEXT, Phone TEXT, Picture TEXT, Nationality TEXT)")
        for (i in listOfItems) {
            db.execSQL("INSERT INTO users VALUES (${i.name.first},${i.name.last},${i.location.country}," +
                    "${i.location.city},${i.location.street},${i.email},${i.login.username},${i.login.password},${i.dob},${i.phone},${i.picture.large},${i.nat});")
        }
        val query: Cursor = db.rawQuery("SELECT * FROM users;", null)
        var temp=0
        while (query.moveToNext()) {
            listOfItems2.add( ShortData(query.getString(0),query.getString(1),query.getString(2),query.getString(3),query.getString(4),query.getString(5),query.getString(6),query.getString(7),query.getString(8),
                    query.getString(9),query.getString(10),query.getString(11)))
            tv1.append("Name: ${listOfItems2[temp].firstName} Age: ${listOfItems2[temp].dob}\n")
            temp++
        }
        query.close()
        db.close()
    }
}