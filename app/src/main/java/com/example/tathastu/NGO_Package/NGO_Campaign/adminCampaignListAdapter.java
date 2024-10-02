package com.example.tathastu.NGO_Package.NGO_Campaign;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Campaign.campaigns;

import java.util.ArrayList;


public class adminCampaignListAdapter extends ArrayAdapter {

    private final Activity contex;
    ArrayList<campaigns> campaignsList;
    public adminCampaignListAdapter(Activity context, ArrayList<campaigns> campaignsList){
        super(context, R.layout.cardview_campaign_list,campaignsList);
        this.contex = context;
        this.campaignsList = campaignsList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =contex.getLayoutInflater();
        View rowview = inflater.inflate(R.layout.cardview_campaign_list,null,true);
        TextView cnameText = (TextView) rowview.findViewById(R.id.txt_cname);
        TextView onameText = (TextView) rowview.findViewById(R.id.txt_oname);
        ImageView cimage= (ImageView) rowview.findViewById(R.id.campaignimage);

        campaigns c = campaignsList.get(position);
        cnameText.setText(c.getName());
        onameText.setText(c.getOrganizer_name());
        Glide.with(contex).load(campaignsList.get(position).getImageUrl()).into(cimage);
        return rowview;

    }
}
