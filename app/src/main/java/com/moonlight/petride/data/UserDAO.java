package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.moonlight.petride.data.DatabaseContract.UserEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase UserDAO: Maneja las operaciones CRUD para la tabla de Usuarios.
 * DAO significa Data Access Object (Objeto de Acceso a Datos).
 */
public class UserDAO {

    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        // Inicializamos el helper para poder abrir la base de datos
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * CREATE: Inserta un nuevo usuario.
     * @return El ID del nuevo registro o -1 si hubo un error.
     */
    public long insertUser(String fullName, String phone, String address, String city, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        // Emparejamos cada columna con el valor que queremos guardar
        values.put(UserEntry.COLUMN_FULL_NAME, fullName);
        values.put(UserEntry.COLUMN_PHONE, phone);
        values.put(UserEntry.COLUMN_ADDRESS, address);
        values.put(UserEntry.COLUMN_CITY, city);
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(UserEntry.COLUMN_PASSWORD, password);

        // Insertamos y cerramos la conexión
        long id = db.insert(UserEntry.TABLE_NAME, null, values);
        db.close(); 
        return id;
    }

    /**
     * Verifica si las credenciales de un usuario son correctas.
     * @param email Email ingresado.
     * @param password Contraseña ingresada.
     * @return true si coinciden, false de lo contrario.
     */
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = UserEntry.COLUMN_EMAIL + " = ? AND " + UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };
        
        Cursor cursor = db.query(UserEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        
        return exists;
    }

    /**
     * READ: Obtiene todos los usuarios de la base de datos.
     */
    public List<String> getAllUsers() {
        List<String> usersList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Consultamos todos los campos de la tabla
        Cursor cursor = db.query(UserEntry.TABLE_NAME, null, null, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Extraemos los datos del cursor (usando el índice de la columna)
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_FULL_NAME));
                    usersList.add(name);
                } while (cursor.moveToNext());
            }
        } finally {
            // Siempre cerramos el cursor para liberar memoria
            if (cursor != null) cursor.close();
            db.close();
        }
        return usersList;
    }

    /**
     * UPDATE: Actualiza los datos de un usuario existente.
     */
    public int updateUser(long id, String fullName, String phone, String address, String city, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(UserEntry.COLUMN_FULL_NAME, fullName);
        values.put(UserEntry.COLUMN_PHONE, phone);
        values.put(UserEntry.COLUMN_ADDRESS, address);
        values.put(UserEntry.COLUMN_CITY, city);
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(UserEntry.COLUMN_PASSWORD, password);

        // Definimos qué registro actualizar usando el _ID
        String selection = UserEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return count; // Retorna cuántas filas se actualizaron
    }

    /**
     * DELETE: Elimina un usuario por su ID.
     */
    public void deleteUser(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = UserEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        db.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
