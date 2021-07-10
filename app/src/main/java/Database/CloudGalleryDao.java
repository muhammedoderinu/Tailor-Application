package Database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface CloudGalleryDao {
    @Query("Select * from CloudGallery")
    List<CloudGallery> getCloudGalleryList();
    @Insert
    void insertCloudGallery(CloudGallery cloudGallery);
    @Update
    void updateCloudGallery(CloudGallery cloudGallery);
    @Delete
    void deleteCloudGallery(CloudGallery cloudGallery);
}
