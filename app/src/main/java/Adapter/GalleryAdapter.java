package Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hfad.tailorme.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    ArrayList<String> imagesUrl;
    Listener listener;

    public interface Listener{
        void onclick(int position);
        void onLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private MaterialCardView cardview;

        public ViewHolder(MaterialCardView v){
            super(v);
            cardview = v;
        }
    }

    public GalleryAdapter(ArrayList<String> imagesUrl){
        this.imagesUrl = imagesUrl;
    }

    @Override
    public int getItemCount(){
        return imagesUrl.size();
    }

    public void setListener(GalleryAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        MaterialCardView cv = (MaterialCardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.galleryview, parent, false);
        return new ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        MaterialCardView cardview = holder.cardview;
        ImageView imageView = (ImageView) cardview.findViewById(R.id.galleryCardView);
        // load image from file
        File imgFile = new File(imagesUrl.get(position));

        if(imgFile.exists()){

            Log.d("testingImage",imagesUrl.get(position));

            Picasso.get()
                    .load(imgFile)
                    .resize(250,250)
                    .centerCrop()
                    .into(imageView);

           // Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
           // imageView.setImageBitmap(myBitmap);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onclick(position);

                    }
                }
            });
            cardview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!=null){
                        listener.onLongClick(position);
                        cardview.setChecked(!cardview.isChecked());
                    }

                    return false;
                }

            });



        }
    }

}
