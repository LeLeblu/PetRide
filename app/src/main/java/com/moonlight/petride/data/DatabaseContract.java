package com.moonlight.petride.data;

import android.provider.BaseColumns;

/**
 * Clase Contrato: Define los nombres de las tablas y columnas de la base de datos.
 * Implementa BaseColumns para heredar el campo _ID automáticamente.
 */
public final class DatabaseContract {

    // Constructor privado para evitar que alguien instancie la clase accidentalmente
    private DatabaseContract() {}

    /* Definición de la tabla User */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

    /* Definición de la tabla Pet */
    public static class PetEntry implements BaseColumns {
        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_OWNER_ID = "owner_id"; // Foreign Key a User
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_CARE_TIPS = "care_tips"; // Opcional
        public static final String COLUMN_IMAGE = "image"; // Nuevo campo para la imagen (URI o path)
    }

    /* Definición de la tabla Ride */
    public static class RideEntry implements BaseColumns {
        public static final String TABLE_NAME = "rides";
        public static final String COLUMN_PET_ID = "pet_id"; // Foreign Key a Pet
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PICKUP_LOCATION = "pickup_location";
        public static final String COLUMN_RIDE_STATE = "ride_state";
    }

    /* Definición de la tabla Session para el manejo de sesión única */
    public static class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String COLUMN_USER_ID = "user_id"; // Foreign Key a User
    }
}
