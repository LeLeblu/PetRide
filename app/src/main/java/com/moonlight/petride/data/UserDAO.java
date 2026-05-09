package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moonlight.petride.data.DatabaseContract.UserEntry;

/**
 * DAO de usuario: operaciones de autenticacion y registro.
 */
public class UserDAO {

    private final DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long insertUser(String fullName, String phone, String address, String city, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_FULL_NAME, fullName);
        values.put(UserEntry.COLUMN_PHONE, phone);
        values.put(UserEntry.COLUMN_ADDRESS, address);
        values.put(UserEntry.COLUMN_CITY, city);
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(UserEntry.COLUMN_PASSWORD, password);

        long id = db.insert(UserEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long checkUserCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long userId = -1;

        String selection = UserEntry.COLUMN_EMAIL + " = ? AND " + UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                UserEntry.TABLE_NAME,
                new String[]{UserEntry._ID},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(UserEntry._ID));
            }
            cursor.close();
        }
        db.close();
        return userId;
    }
}
