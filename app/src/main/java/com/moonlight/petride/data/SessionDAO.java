package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.moonlight.petride.data.DatabaseContract.SessionEntry;

/**
 * SessionDAO: Clase encargada de gestionar la sesión del usuario en la base de datos SQLite.
 * Se utiliza para mantener la persistencia de quién ha iniciado sesión sin usar SharedPreferences.
 */
public class SessionDAO {
    private DatabaseHelper dbHelper;

    public SessionDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Inicia una sesión para el usuario.
     * TRUCO DIDÁCTICO: Para asegurar que solo exista un usuario logueado a la vez,
     * primero vaciamos la tabla (delete) y luego insertamos el nuevo registro.
     * Así evitamos conflictos y simplificamos el manejo de la sesión única.
     */
    public void login(long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // 1. Limpiamos cualquier rastro de sesiones anteriores (Cerrar sesión previa)
        db.delete(SessionEntry.TABLE_NAME, null, null);
        
        // 2. Insertamos el ID del nuevo usuario logueado
        ContentValues values = new ContentValues();
        values.put(SessionEntry.COLUMN_USER_ID, userId);
        db.insert(SessionEntry.TABLE_NAME, null, values);
        
        db.close();
    }

    /**
     * Obtiene el ID del usuario actualmente logueado.
     * @return El ID del usuario o -1 si no hay ninguna sesión activa.
     */
    public long getLoggedUserId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long userId = -1;

        Cursor cursor = db.query(
                SessionEntry.TABLE_NAME,
                new String[]{SessionEntry.COLUMN_USER_ID},
                null, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(SessionEntry.COLUMN_USER_ID));
            cursor.close();
        }
        
        db.close();
        return userId;
    }

    /**
     * Cierra la sesión actual eliminando todos los registros de la tabla session.
     */
    public void logout() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SessionEntry.TABLE_NAME, null, null);
        db.close();
    }
}
