package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.moonlight.petride.data.DatabaseContract.PetEntry;

/**
 * Clase PetDAO: Maneja las operaciones CRUD para la tabla de Mascotas.
 * Aplicamos el patrón DAO para separar la lógica de negocio de la base de datos.
 */
public class PetDAO {

    private DatabaseHelper dbHelper;

    public PetDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * CREATE: Inserta una nueva mascota en la base de datos.
     * @return El ID de la fila insertada o -1 si hubo un fallo.
     */
    public long insertPet(long ownerId, String name, String breed, int age, String careTips, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        // Empaquetamos los datos en un objeto ContentValues
        values.put(PetEntry.COLUMN_OWNER_ID, ownerId);
        values.put(PetEntry.COLUMN_NAME, name);
        values.put(PetEntry.COLUMN_BREED, breed);
        values.put(PetEntry.COLUMN_AGE, age);
        values.put(PetEntry.COLUMN_CARE_TIPS, careTips);
        values.put(PetEntry.COLUMN_IMAGE, image);

        long id = db.insert(PetEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * READ: Obtiene una mascota específica por su ID.
     * @return Un Cursor con el resultado (debe cerrarse después de usar).
     */
    public Cursor getPetById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        // Retornamos el cursor directamente. Recuerda cerrarlo en la UI o donde se llame.
        return db.query(PetEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    /**
     * READ: Obtiene todas las mascotas.
     */
    public Cursor getAllPets() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(PetEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * UPDATE: Actualiza los datos de una mascota.
     */
    public int updatePet(long id, String name, String breed, int age, String careTips, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(PetEntry.COLUMN_NAME, name);
        values.put(PetEntry.COLUMN_BREED, breed);
        values.put(PetEntry.COLUMN_AGE, age);
        values.put(PetEntry.COLUMN_CARE_TIPS, careTips);
        values.put(PetEntry.COLUMN_IMAGE, image);

        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int rowsAffected = db.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return rowsAffected;
    }

    /**
     * DELETE: Elimina una mascota por su ID.
     */
    public int deletePet(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        int rowsDeleted = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rowsDeleted;
    }
}
