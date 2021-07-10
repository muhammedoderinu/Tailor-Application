package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.tailorme.R;

import java.util.ArrayList;

public class CustomerFragmentAdapter  extends
        RecyclerView.Adapter<CustomerFragmentAdapter.ViewHolder> {
    private ArrayList<String> customer;
    Listener listener;
    ArrayList<String> name;
    ArrayList<String> phoneNumber;
    ArrayList<String> dates;


   public interface Listener{
        void onClick(int position);

        void onDelete(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView=v;
        }
    }

    public CustomerFragmentAdapter(ArrayList<String> name,ArrayList<String>
            phoneNumber, ArrayList<String> dates){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dates = dates;
    }
    @Override
    public int getItemCount(){
        return name.size();
    }

    public void setListener(CustomerFragmentAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public CustomerFragmentAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customercard, parent, false);
        return new CustomerFragmentAdapter.ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(CustomerFragmentAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView customerName = (TextView) cardView.findViewById(R.id.customerCardName);
       TextView customerPhoneNumber = (TextView) cardView.findViewById(R.id.phoneContact);
       TextView dateView  = cardView.findViewById(R.id.date);


        customerName.setText(name.get(position));
        customerPhoneNumber.setText(phoneNumber.get(position));
        dateView.setText(dates.get(position));
        //customerStyle.setText(style.get(position));











    }


}
