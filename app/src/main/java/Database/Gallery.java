package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
@Entity(tableName = "gallery",indices = {@Index(value={"filePath"},unique = true)})
public class Gallery {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name ="filePath")
    public String filePath;


    public String getFilepath(){
        return filePath;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public int getId(){return id;}
}
