package Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface GalleryDao {
    @Query("Select * from Gallery")
    List<Gallery> getGalleryList();
    @Insert
    void insertGallery(Gallery gallery);
    @Update
    void updateGallery(Gallery gallery);
    @Delete
    void deleteGallery(Gallery gallery);
}
