package com.example.films;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    //private LayoutInflater inflater;
    private Context mContext;
    private List<Films> listFilms;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick( int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    DataAdapter(Context context, List<Films> listFilms) {
        this.listFilms = listFilms;
        this.mContext = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.film_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Films films = listFilms.get(position);
        String imageUrl = films.getImage_url();
        String creatorName = films.getLocalized_name();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);
        holder.nameView.setText(creatorName);
    }

    @Override
    public int getItemCount() {
        return listFilms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            nameView = view.findViewById(R.id.name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }


}

