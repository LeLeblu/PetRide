package com.moonlight.petride.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.moonlight.petride.data.DatabaseContract.UserEntry;
import com.moonlight.petride.data.DatabaseContract.PetEntry;
import com.moonlight.petride.data.DatabaseContract.RideEntry;

/**
 * Clase DatabaseHelper: Gestiona la creación y actualización de la base de datos.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "PetRide.db";
    private static final int DATABASE_VERSION = 1;

    // Sentencia SQL para crear la tabla de Usuarios
    private static final String SQL_CREATE_USERS_TABLE = 
        "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
        UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        UserEntry.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
        UserEntry.COLUMN_PHONE + " TEXT NOT NULL, " +
        UserEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
        UserEntry.COLUMN_CITY + " TEXT NOT NULL, " +
        UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
        UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL);";

    // Sentencia SQL para crear la tabla de Mascotas (Pets)
    private static final String SQL_CREATE_PETS_TABLE = 
        "CREATE TABLE " + PetEntry.TABLE_NAME + " (" +
        PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        PetEntry.COLUMN_OWNER_ID + " INTEGER NOT NULL, " +
        PetEntry.COLUMN_NAME + " TEXT NOT NULL, " +
        PetEntry.COLUMN_BREED + " TEXT NOT NULL, " +
        PetEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
        PetEntry.COLUMN_CARE_TIPS + " TEXT, " + // Opcional
        "FOREIGN KEY (" + PetEntry.COLUMN_OWNER_ID + ") REFERENCES " + 
        UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

    // Sentencia SQL para crear la tabla de Paseos (Rides)
    private static final String SQL_CREATE_RIDES_TABLE = 
        "CREATE TABLE " + RideEntry.TABLE_NAME + " (" +
        RideEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        RideEntry.COLUMN_PET_ID + " INTEGER NOT NULL, " +
        RideEntry.COLUMN_DATE + " TEXT NOT NULL, " +
        RideEntry.COLUMN_PICKUP_LOCATION + " TEXT NOT NULL, " +
        RideEntry.COLUMN_RIDE_STATE + " TEXT NOT NULL, " + // O Entero, según prefieras
        "FOREIGN KEY (" + RideEntry.COLUMN_PET_ID + ") REFERENCES " + 
        PetEntry.TABLE_NAME + "(" + PetEntry._ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta las sentencias SQL para crear las tablas
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_RIDES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En una aplicación real, se debería migrar la información.
        // Aquí simplemente eliminamos y volvemos a crear las tablas.
        db.execSQL("DROP TABLE IF EXISTS " + RideEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Habilitar restricciones de clave foránea en SQLite
        db.setForeignKeyConstraintsEnabled(true);
    }
}
