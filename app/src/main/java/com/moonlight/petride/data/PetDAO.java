package com.moonlight.petride.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moonlight.petride.data.DatabaseContract.PetEntry;
import com.moonlight.petride.data.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO de mascotas: CRUD de la tabla pets.
 */
public class PetDAO {

    private final DatabaseHelper dbHelper;

    public PetDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long insertPet(long ownerId, String name, String breed, int age, String careTips, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
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

    public Pet getPetById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(PetEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Pet pet = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                pet = cursorToPet(cursor);
            }
            cursor.close();
        }
        db.close();
        return pet;
    }

    public List<Pet> getPetsByUserId(long userId) {
        List<Pet> pets = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = PetEntry.COLUMN_OWNER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(PetEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    pets.add(cursorToPet(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return pets;
    }

    public int updatePet(long id, String name, String breed, int age, String careTips, String image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_NAME, name);
        values.put(PetEntry.COLUMN_BREED, breed);
        values.put(PetEntry.COLUMN_AGE, age);
        values.put(PetEntry.COLUMN_CARE_TIPS, careTips);
        values.put(PetEntry.COLUMN_IMAGE, image);

        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int rowsAffected = db.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return rowsAffected;
    }

    public int deletePet(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = PetEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int rowsDeleted = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rowsDeleted;
    }

    private Pet cursorToPet(Cursor cursor) {
        Pet pet = new Pet();
        pet.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PetEntry._ID)));
        pet.setOwnerId(cursor.getLong(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_OWNER_ID)));
        pet.setName(cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_NAME)));
        pet.setBreed(cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_BREED)));
        pet.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_AGE)));
        pet.setCareTips(cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_CARE_TIPS)));
        pet.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_IMAGE)));
        return pet;
    }
}
