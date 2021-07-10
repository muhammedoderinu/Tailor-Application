package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName="customer",indices = {@Index(value={"time"},unique = true)})
public class Customer {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="customerName")
    public String customerName;
    @ColumnInfo(name="phoneNumber")
    public String phoneNumber;
    @ColumnInfo(name="style")
    public String style;
    @ColumnInfo(name="inProgress")
    public Boolean inProgress;
    @ColumnInfo(name="back")
    public String back;
    @ColumnInfo(name="bust")
    public String bust;
    @ColumnInfo(name="waist")
    public String waist;
    @ColumnInfo(name="hips")
    public String hips;
    @ColumnInfo(name="sleeve")
    public String sleeve;
    @ColumnInfo(name="round sleeve")
    public String roundSleeve;
    @ColumnInfo(name="full length of gown")
    public String fullLengthOfGown;
    @ColumnInfo(name="length of skirt")
    public String lengthOfSkirt;
    @ColumnInfo(name="length of blouse")
    public String lengthOfBlouse;
    @ColumnInfo(name="date")
    public String date;
    @ColumnInfo(name="time")
    public String time;
    @ColumnInfo(name="longtime")
    public long longTime;

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber( String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerStyle(){
        return style;
    }

    public void setCustomerStyle(String style) {
        this.style = style;
    }

    public String getBack(){
        return back;
    }
    public void setBack(String back){
        this.back = back;
    }

    public String getBust(){
        return bust;
    }
    public void setBust(String bust){
        this.bust = bust;
    }

    public String getWaist(){
        return waist;
    }
    public void setWaist(String waist){
        this.waist = waist;
    }

    public String getCustomerHips(){
        return hips;
    }
    public void setCustomerHips(String hips){
        this.hips = hips;
    }
    public String getSleeve(){
        return sleeve;
    }
    public void setSleeve(String sleeve){
        this.sleeve = sleeve;
    }
    public String getRoundSleeve(){
        return roundSleeve;
    }
    public void setRoundSleeve(String roundSleeve){
        this.roundSleeve = roundSleeve;
    }
    public void setInProgress(Boolean inProgress){
        this.inProgress = inProgress;
    }

    public Boolean getInProgress(){
        return inProgress;
    }

    public void setFullLengthOfGown(String fullLengthOfGown){
        this.fullLengthOfGown = fullLengthOfGown;
    }

    public String getFullLengthOfGown(){
        return fullLengthOfGown;
    }
    public void setLengthOfSkirt(String lengthOfSkirt){
        this.lengthOfSkirt = lengthOfSkirt;
    }

    public String getLengthOfSkirt(){
        return lengthOfSkirt;
    }

    public void setLengthOfBlouse(String lengthOfBlouse){
        this.lengthOfBlouse = lengthOfBlouse;
    }

    public String getLengthOfBlouse(){
        return lengthOfBlouse;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getLongTime() {
        return longTime;
    }
    public void setLongTime(long time) {
        this.longTime = time;
    }







}
