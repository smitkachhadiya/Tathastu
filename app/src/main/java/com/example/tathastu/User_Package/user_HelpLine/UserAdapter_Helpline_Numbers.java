package com.example.tathastu.User_Package.user_HelpLine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;

import java.util.List;

public class UserAdapter_Helpline_Numbers extends RecyclerView.Adapter<UserAdapter_Helpline_Numbers.HelplineViewHolder> {

    private List<UserModel_Helpline_Numbers> helplineList;
    private Context context;
    private int expandedPosition = -1;

    public UserAdapter_Helpline_Numbers(List<UserModel_Helpline_Numbers> helplineList, Context context) {
        this.helplineList = helplineList;
        this.context = context;
    }

//    public void setData(List<UserModel_Helpline_Numbers> dataList) {
//        this.helplineList = helplineList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public HelplineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_helpline_numbers, parent, false);
        return new HelplineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelplineViewHolder holder, int position) {
        UserModel_Helpline_Numbers helpline = helplineList.get(position);

        // Bind your data to the ViewHolder here
        holder.txt_helpline_name.setText(helpline.getName());
        holder.txt_helpline_mno.setText(helpline.getNumber());
        holder.txt_helpline_category.setText(helpline.getCategory());

        // Set a click listener for the copy button
//        holder.btn_copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle copy to clipboard action
//                copyToClipboard(context, helpline.getNumber());
//            }
//        });

        // Set a click listener for the helpline name to handle expansion
        holder.txt_helpline_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExpansion(holder, holder.getAdapterPosition());
            }
        });

        // Handle expansion logic
        if (holder.getAdapterPosition() == expandedPosition) {
            // Show expanded details
            holder.layout_helpline.setVisibility(View.VISIBLE);
            // Set the up arrow drawable
            holder.txt_helpline_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_up_24, 0);
        } else {
            // Hide expanded details
            holder.layout_helpline.setVisibility(View.GONE);
            // Set the down arrow drawable
            holder.txt_helpline_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_down_24, 0);
        }
    }

    private void handleExpansion(HelplineViewHolder holder, int position) {
        if (expandedPosition == position) {
            // Collapse the expanded item
            expandedPosition = -1;
            notifyItemChanged(position);
        } else {
            // Expand the clicked item
            int oldExpandedPosition = expandedPosition;
            expandedPosition = position;
            notifyItemChanged(oldExpandedPosition);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return helplineList.size();
    }

    public static class HelplineViewHolder extends RecyclerView.ViewHolder {
        // Declare your CardView elements here
        AppCompatTextView txt_helpline_name, txt_helpline_mno, txt_helpline_category;
//        ImageButton btn_copy;
        LinearLayout layout_helpline; // Add this line

        public HelplineViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your CardView elements here
            txt_helpline_name = itemView.findViewById(R.id.txt_helpline_name);
            txt_helpline_mno = itemView.findViewById(R.id.txt_helpline_mno);
            txt_helpline_category = itemView.findViewById(R.id.txt_helpline_category);
//            btn_copy = itemView.findViewById(R.id.btn_copy);
            layout_helpline = itemView.findViewById(R.id.layout_helpline); // Add this line
        }
    }
    private static void copyToClipboard(Context context, String phoneNumber) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Phone Number", phoneNumber);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            showToast(context, "Phone number copied to clipboard");
        }
    }

    private static void showToast(Context context, final String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
