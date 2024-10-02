package com.example.tathastu.User_Package.user_Quotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<itemmodel> quotes;

    public CardStackAdapter(List<itemmodel> quotes) {
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_quotes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemmodel quote = quotes.get(position);
        holder.bind(quote, position);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quoteTextView;
        ShapeableImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(R.id.txt_quote);
            imageView = itemView.findViewById(R.id.image);
        }

        void bind(itemmodel quote, int position) {
            quoteTextView.setText(quote.getQuote());

            // Use modulo operation to ensure it's within bounds
            int imageResource = getImageResource(position);
            int textColorResource = getTextColorResource(position);

            imageView.setImageResource(imageResource);
            quoteTextView.setTextColor(itemView.getContext().getResources().getColor(textColorResource));
        }

        // Helper method to get the image resource based on position
        private int getImageResource(int position) {

            int[] imageResources = {R.drawable.banner_allquote_bg_1, R.drawable.banner_allquote_bg_3, R.drawable.banner_allquote_bg_2};
            return imageResources[position % imageResources.length];
        }

        // Helper method to get the text color resource based on position
        private int getTextColorResource(int position) {
            int[] textColorResources = {R.color.first, R.color.second, R.color.third};
            return textColorResources[position % textColorResources.length];
        }
    }
}
