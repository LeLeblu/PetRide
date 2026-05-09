package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moonlight.petride.data.DatabaseContract.SessionEntry;

/**
 * DAO de sesion: guarda que usuario esta autenticado.
 */
public class SessionDAO {

    private final DatabaseHelper dbHelper;

    public SessionDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void login(long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Solo permitimos una sesion activa.
        db.delete(SessionEntry.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(SessionEntry.COLUMN_USER_ID, userId);
        db.insert(SessionEntry.TABLE_NAME, null, values);
        db.close();
    }

    public long getLoggedUserId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long userId = -1;

        Cursor cursor = db.query(
                SessionEntry.TABLE_NAME,
                new String[]{SessionEntry.COLUMN_USER_ID},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(SessionEntry.COLUMN_USER_ID));
            }
            cursor.close();
        }
        db.close();
        return userId;
    }

    public void logout() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SessionEntry.TABLE_NAME, null, null);
        db.close();
    }
}
