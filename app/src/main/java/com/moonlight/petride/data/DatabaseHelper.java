package com.moonlight.petride.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.moonlight.petride.data.DatabaseContract.UserEntry;
import com.moonlight.petride.data.DatabaseContract.PetEntry;
import com.moonlight.petride.data.DatabaseContract.RideEntry;
import com.moonlight.petride.data.DatabaseContract.SessionEntry;

/**
 * Clase DatabaseHelper: Gestiona la creación y actualización de la base de datos.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "PetRide.db";
    private static final int DATABASE_VERSION = 3; // Incrementada de 2 a 3 para la tabla session

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
        PetEntry.COLUMN_IMAGE + " TEXT, " + // Nuevo campo para la imagen
        "FOREIGN KEY (" + PetEntry.COLUMN_OWNER_ID + ") REFERENCES " + 
        UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

    // Sentencia SQL para crear la tabla de Paseos (Rides)
    private static final String SQL_CREATE_RIDES_TABLE = 
        "CREATE TABLE " + RideEntry.TABLE_NAME + " (" +
        RideEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        RideEntry.COLUMN_PET_ID + " INTEGER NOT NULL, " +
        RideEntry.COLUMN_DATE + " TEXT NOT NULL, " +
        RideEntry.COLUMN_PICKUP_LOCATION + " TEXT NOT NULL, " +
        RideEntry.COLUMN_RIDE_STATE + " TEXT NOT NULL, " +
        "FOREIGN KEY (" + RideEntry.COLUMN_PET_ID + ") REFERENCES " + 
        PetEntry.TABLE_NAME + "(" + PetEntry._ID + "));";

    // Sentencia SQL para crear la tabla de Sesión
    private static final String SQL_CREATE_SESSION_TABLE = 
        "CREATE TABLE " + SessionEntry.TABLE_NAME + " (" +
        SessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        SessionEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
        "FOREIGN KEY (" + SessionEntry.COLUMN_USER_ID + ") REFERENCES " + 
        UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta las sentencias SQL para crear las tablas
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_RIDES_TABLE);
        db.execSQL(SQL_CREATE_SESSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + PetEntry.TABLE_NAME + " ADD COLUMN " + PetEntry.COLUMN_IMAGE + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL(SQL_CREATE_SESSION_TABLE);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Habilitar restricciones de clave foránea en SQLite
        db.setForeignKeyConstraintsEnabled(true);
    }
}
