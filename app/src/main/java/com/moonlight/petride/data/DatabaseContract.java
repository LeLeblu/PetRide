package com.moonlight.petride.data;

import android.provider.BaseColumns;

/**
 * Contrato de base de datos: define tablas y columnas.
 */
public final class DatabaseContract {

    private DatabaseContract() {
        // Evita instancias accidentales.
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

    public static class PetEntry implements BaseColumns {
        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_OWNER_ID = "owner_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_CARE_TIPS = "care_tips";
        public static final String COLUMN_IMAGE = "image";
    }

    public static class RideEntry implements BaseColumns {
        public static final String TABLE_NAME = "rides";
        public static final String COLUMN_PET_ID = "pet_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PICKUP_LOCATION = "pickup_location";
        public static final String COLUMN_RIDE_STATE = "ride_state";
    }

    public static class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String COLUMN_USER_ID = "user_id";
    }
}
