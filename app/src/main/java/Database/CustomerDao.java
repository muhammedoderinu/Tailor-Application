package Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface CustomerDao {
    @Query("Select * from Customer")
    List<Customer> getCustomerList();
    @Insert
    void insertCustomer(Customer customer);
    @Update
    void updateCustomer(Customer customer);
    @Delete
    void deleteCustomer(Customer customer);
    @Query("Select * from Customer where phoneNumber like :search")
     List<Customer> getPhone(String search);
    @Query("select * from Customer where longtime between :dayst and :dayed")
    List<Customer> getDateSearch(long dayst, long dayed);
    @Query("select * from Customer where customerName like :search")
    List<Customer> getNameSearch(String search);

}
