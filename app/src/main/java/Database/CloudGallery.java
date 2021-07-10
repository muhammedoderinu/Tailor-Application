package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="cloudGallery")
public class CloudGallery {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "filePath")
    public String filePath;
    public String getFilepath(){
        return filePath;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public int getId(){return id;}


}
