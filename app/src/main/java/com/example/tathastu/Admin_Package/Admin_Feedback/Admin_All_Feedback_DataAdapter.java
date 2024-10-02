package com.example.tathastu.Admin_Package.Admin_Feedback;

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

public class Admin_All_Feedback_DataAdapter extends RecyclerView.Adapter<Admin_All_Feedback_DataAdapter.FeedbackViewHolder> {

    private List<Admin_All_Feedback_DataModel> feedbackDataModelList;
    private Context context;
    private int expandedPosition = -1;

    // Modify the constructor to accept a Context parameter
    public Admin_All_Feedback_DataAdapter(Context context, List<Admin_All_Feedback_DataModel> feedbackDataModelList) {
        this.context = context;
        this.feedbackDataModelList = feedbackDataModelList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_admin_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Admin_All_Feedback_DataModel feedback = feedbackDataModelList.get(position);

        // Bind your data to the ViewHolder here
        holder.txt_feedback_name.setText(feedback.getName());
        holder.txt_feedback_email.setText(feedback.getEmail());
        holder.txt_feedback_msg.setText(feedback.getFeedback());

        // Set a click listener for the copy button
//        holder.btn_copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle copy to clipboard action
//                copyToClipboard(context, helpline.getNumber());
//            }
//        });

        // Set a click listener for the helpline name to handle expansion
        holder.txt_feedback_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExpansion(holder, holder.getAdapterPosition());
            }
        });

        // Handle expansion logic
        if (holder.getAdapterPosition() == expandedPosition) {
            // Show expanded details
            holder.layout_feedback.setVisibility(View.VISIBLE);
            // Set the up arrow drawable
            holder.txt_feedback_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_up_24, 0);
        } else {
            // Hide expanded details
            holder.layout_feedback.setVisibility(View.GONE);
            // Set the down arrow drawable
            holder.txt_feedback_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_down_24, 0);
        }
    }

    private void handleExpansion(FeedbackViewHolder holder, int position) {
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
        return feedbackDataModelList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        // Declare your CardView elements here
        AppCompatTextView txt_feedback_name, txt_feedback_email, txt_feedback_msg;
//        ImageButton btn_copy;
        LinearLayout layout_feedback; // Add this line

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your CardView elements here
            txt_feedback_name = itemView.findViewById(R.id.txt_feedback_name);
            txt_feedback_email = itemView.findViewById(R.id.txt_feedback_email);
            txt_feedback_msg = itemView.findViewById(R.id.txt_feedback_msg);
//            btn_copy = itemView.findViewById(R.id.btn_copy);
            layout_feedback = itemView.findViewById(R.id.layout_feedback); // Add this line
        }
    }

}
