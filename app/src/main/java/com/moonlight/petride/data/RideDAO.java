package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.moonlight.petride.data.DatabaseContract.PetEntry;
import com.moonlight.petride.data.DatabaseContract.RideEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO de paseos: CRUD basico para rides.
 */
public class RideDAO {

    private final DatabaseHelper dbHelper;

    public RideDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

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

    public List<RideWithPet> getRidesByUserId(long userId) {
        List<RideWithPet> rides = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT r." + RideEntry.COLUMN_DATE + ", r." + RideEntry.COLUMN_PICKUP_LOCATION + ", " +
                "r." + RideEntry.COLUMN_RIDE_STATE + ", p." + PetEntry.COLUMN_NAME + " " +
                "FROM " + RideEntry.TABLE_NAME + " r " +
                "INNER JOIN " + PetEntry.TABLE_NAME + " p ON r." + RideEntry.COLUMN_PET_ID + " = p." + PetEntry._ID + " " +
                "WHERE p." + PetEntry.COLUMN_OWNER_ID + " = ? " +
                "ORDER BY r." + RideEntry._ID + " DESC";

        android.database.Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                String fechaHora = cursor.getString(0);
                String direccion = cursor.getString(1);
                String estado = cursor.getString(2);
                String nombreMascota = cursor.getString(3);
                rides.add(new RideWithPet(nombreMascota, fechaHora, direccion, estado));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rides;
    }

    public int updateRide(long id, String date, String pickupLocation, String rideState) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RideEntry.COLUMN_DATE, date);
        values.put(RideEntry.COLUMN_PICKUP_LOCATION, pickupLocation);
        values.put(RideEntry.COLUMN_RIDE_STATE, rideState);

        String selection = RideEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int rows = db.update(RideEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return rows;
    }

    public int deleteRide(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RideEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int rows = db.delete(RideEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rows;
    }

    public static class RideWithPet {
        private final String petName;
        private final String dateTime;
        private final String pickupLocation;
        private final String rideState;

        public RideWithPet(String petName, String dateTime, String pickupLocation, String rideState) {
            this.petName = petName;
            this.dateTime = dateTime;
            this.pickupLocation = pickupLocation;
            this.rideState = rideState;
        }

        public String getPetName() {
            return petName;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public String getRideState() {
            return rideState;
        }
    }
}
