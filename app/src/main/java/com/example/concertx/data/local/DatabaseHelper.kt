package com.example.concertx.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "concertx.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE Users (
                UserID INTEGER PRIMARY KEY AUTOINCREMENT,
                Email VARCHAR(255) NOT NULL,
                Username VARCHAR(255) NOT NULL UNIQUE,
                PhoneNumber VARCHAR(255) NOT NULL,
                Password VARCHAR(255) NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Concerts (
                ConcertID INTEGER PRIMARY KEY,
                Title VARCHAR(255) NOT NULL,
                Artist VARCHAR(255) NOT NULL,
                Venue VARCHAR(255) NOT NULL,
                City VARCHAR(255) NOT NULL,
                Latitude REAL(10) NOT NULL,
                Longitude REAL(10) NOT NULL,
                Date VARCHAR(255) NOT NULL,
                Price INTEGER(10) NOT NULL,
                Image VARCHAR(255),
                Description VARCHAR(255),
                Stock INTEGER(10) NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE "Transaction" (
                TransactionID INTEGER PRIMARY KEY AUTOINCREMENT,
                UserId INTEGER(10) NOT NULL,
                ConcertId INTEGER(10) NOT NULL,
                Quantity INTEGER(10) NOT NULL,
                FOREIGN KEY (UserId) REFERENCES Users(UserID),
                FOREIGN KEY (ConcertId) REFERENCES Concerts(ConcertID)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS \"Transaction\"")
        db.execSQL("DROP TABLE IF EXISTS Concerts")
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun isUsernameExists(username: String): Boolean {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM Users WHERE Username = ?", arrayOf(username)
        )
        val exists = result.count > 0
        result.close()
        return exists
    }

    fun getPhoneByUsername(username: String): String? {
        val result = readableDatabase.rawQuery(
            "SELECT PhoneNumber FROM Users WHERE Username = ?", arrayOf(username)
        )
        var phone: String? = null
        if (result.moveToFirst()) {
            phone = result.getString(0)
        }
        result.close()
        return phone
    }

    fun insertUser(username: String, email: String, phone: String, password: String): Boolean {
        return try {
            writableDatabase.execSQL(
                "INSERT INTO Users (Username, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?)",
                arrayOf(username, email, phone, password)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    fun checkLogin(username: String, password: String): Boolean {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM Users WHERE Username = ? AND Password = ?",
            arrayOf(username, password)
        )
        val valid = result.count > 0
        result.close()
        return valid
    }

    fun getUserByUsername(username: String): User? {
        val result = readableDatabase.rawQuery(
            "SELECT Username, Email, PhoneNumber FROM Users WHERE Username = ?",
            arrayOf(username)
        )
        var user: User? = null
        if (result.moveToFirst()) {
            user = User(
                username = result.getString(0),
                email = result.getString(1),
                phone = result.getString(2)
            )
        }
        result.close()
        return user
    }

    fun insertConcert(c: Concert){
        writableDatabase.execSQL(
            "INSERT OR IGNORE INTO Concerts (ConcertID, Title, Artist, Venue, City, Latitude, Longitude, Date, Price, Image, Description, Stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)".trimIndent(),
            arrayOf(c.id, c.title, c.artist, c.venue, c.city, c.latitude, c.longitude,
                c.date, c.price, c.image, c.description, c.stock)
        )
    }

    fun getAllConcerts(): List<Concert>{
        val list = mutableListOf<Concert>()
        val result = readableDatabase.rawQuery("SELECT ConcertID, Title, Artist, Venue, City, Latitude, Longitude, Date, Price, Image, Description, Stock FROM Concerts ORDER BY ConcertID", null)
        while(result.moveToNext()){
            list.add(
                Concert(
                    id = result.getInt(0),
                    title = result.getString(1),
                    artist = result.getString(2),
                    venue = result.getString(3),
                    city = result.getString(4),
                    latitude = result.getDouble(5),
                    longitude = result.getDouble(6),
                    date = result.getString(7),
                    price = result.getInt(8),
                    image = result.getString(9) ?: "",
                    description = result.getString(10) ?: "",
                    stock = result.getInt(11)
                )
            )
        }
        result.close()
        return list
    }
}