package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.tailorme.R;

import java.util.ArrayList;

public class CustomerResultAdapter
               extends RecyclerView.Adapter<CustomerResultAdapter.ViewHolder>  {

    CustomerFragmentAdapter.Listener listener;
    ArrayList<String> data;
    ArrayList<String> keys;


public interface Listener{
    void onClick(int position);
    void onProgress(int position);
    void onOffProgress(int position);
    void onDelete(int position);
}

public static class ViewHolder extends RecyclerView.ViewHolder{

    private CardView cardView;

    public ViewHolder(CardView v){
        super(v);
        cardView=v;
    }
}

    public CustomerResultAdapter(ArrayList<String> data, ArrayList<String> keys){
        this.data = data;
        this.keys = keys;
    }
    @Override
    public int getItemCount(){
        return data.size();
    }

    public void setListener(CustomerFragmentAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public CustomerResultAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customercard, parent, false);
        return new CustomerResultAdapter.ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(CustomerResultAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView customerName = (TextView) cardView.findViewById(R.id.resultName);
        TextView customerPhoneNumber = (TextView) cardView.findViewById(R.id.result);


        customerName.setText(data.get(position));
        customerPhoneNumber.setText(keys.get(position));
        //customerStyle.setText(style.get(position));











    }


}
