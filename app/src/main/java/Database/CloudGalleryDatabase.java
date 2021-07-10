package Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class CloudGalleryDatabase extends RoomDatabase {
    private static final String DB_NAME = "gallery_db";
    public abstract CloudGalleryDao cloudGalleryDao();
    private static CloudGalleryDatabase instance;

    public static CloudGalleryDatabase getInstance(Context context){
        if(instance==null){
            instance  = Room.databaseBuilder(context.getApplicationContext(),
                    CloudGalleryDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
