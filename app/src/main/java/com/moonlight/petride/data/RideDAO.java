package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.moonlight.petride.data.DatabaseContract.RideEntry;

/**
 * Clase RideDAO: Maneja las operaciones CRUD para la tabla de Paseos (Rides).
 * Sigue el patrón DAO para organizar el código de acceso a datos.
 */
public class RideDAO {

    private DatabaseHelper dbHelper;

    public RideDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * CREATE: Registra un nuevo paseo.
     * @return El ID del nuevo paseo o -1 si hubo error.
     */
    public long insertRide(long petId, String date, String pickupLocation, String rideState) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(RideEntry.COLUMN_PET_ID, petId);
        values.put(RideEntry.COLUMN_DATE, date);
        values.put(RideEntry.COLUMN_PICKUP_LOCATION, pickupLocation);
        values.put(RideEntry.COLUMN_RIDE_STATE, rideState);

        long id = db.insert(RideEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * READ: Obtiene todos los paseos registrados.
     */
    public Cursor getAllRides() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Usamos null en las columnas para traer todos los campos
        return db.query(RideEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * UPDATE: Actualiza el estado o datos de un paseo.
     */
    public int updateRide(long id, String date, String pickupLocation, String rideState) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(RideEntry.COLUMN_DATE, date);
        values.put(RideEntry.COLUMN_PICKUP_LOCATION, pickupLocation);
        values.put(RideEntry.COLUMN_RIDE_STATE, rideState);

        String selection = RideEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int rows = db.update(RideEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return rows;
    }

    /**
     * DELETE: Elimina un paseo por su ID.
     */
    public int deleteRide(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RideEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        int rows = db.delete(RideEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rows;
    }
}
