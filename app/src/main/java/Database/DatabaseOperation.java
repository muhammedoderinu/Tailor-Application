package Database;

import android.content.Context;

import java.util.List;

public class DatabaseOperation {

    public List query(Context context){
        CustomerDatabase appDb = CustomerDatabase.getInstance(context);
        return appDb.customerDao().getCustomerList();

    }

    public void insert(Context context, Customer customer){
        CustomerDatabase appDb = CustomerDatabase.getInstance(context);
        appDb.customerDao().insertCustomer(customer);
    }

    public void delete(Context context, Customer customer){
        CustomerDatabase appDb = CustomerDatabase.getInstance(context);
            appDb.customerDao().deleteCustomer(customer);

    }

    public void update(Context context,Customer customer){
        CustomerDatabase appDb = CustomerDatabase.getInstance(context);
        appDb.customerDao().updateCustomer(customer);

    }




}
