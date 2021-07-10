package Adapter;

import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.tailorme.R;

import java.util.ArrayList;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {


    private ArrayList<String> customer;
    private ArrayList<String> phoneNumber;
    private ArrayList<String> style;
    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView=v;
        }
    }
    public HomePageAdapter(ArrayList<String> customer, ArrayList<String> phoneNumber,
                                  ArrayList<String> style){
        this.customer = customer;
        this.phoneNumber = phoneNumber;
        this.style = style;
    }

    @Override
    public int getItemCount(){
        return customer.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public HomePageAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homerecycler, parent, false);
        return new ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView customerName = (TextView) cardView.findViewById(R.id.customer);
        TextView customerPhoneNumber = (TextView) cardView.findViewById(R.id.phoneNumber);
        TextView customerStyle = (TextView) cardView.findViewById(R.id.style);

        customerName.setText(customer.get(position));
        customerPhoneNumber.setText(customer.get(position));
        customerStyle.setText(customer.get(position));
    }





}
