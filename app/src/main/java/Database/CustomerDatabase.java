package Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities={Customer.class}, version = 7)
public abstract class CustomerDatabase extends RoomDatabase {
    private static final String DB_NAME = "customer_db";
    public abstract CustomerDao customerDao();
    private static CustomerDatabase instance;


    public static synchronized CustomerDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CustomerDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }




}
