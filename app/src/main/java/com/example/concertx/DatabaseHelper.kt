package com.example.concertx

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
                UserID INTEGER(10) PRIMARY KEY AUTOINCREMENT,
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
                ConcertID INTEGER(10) PRIMARY KEY,
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
                TransactionID INTEGER(10) PRIMARY KEY AUTOINCREMENT,
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
}