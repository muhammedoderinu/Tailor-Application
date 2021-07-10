package Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Gallery.class}, version=1)
public abstract class GalleryDatabase extends RoomDatabase {
    private static final String DB_NAME = "gallery_db";
    public abstract GalleryDao galleryDao();
    private static GalleryDatabase instance;

    public static GalleryDatabase getInstance(Context context){
        if(instance==null){
            instance  = Room.databaseBuilder(context.getApplicationContext(),
                    GalleryDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


}
