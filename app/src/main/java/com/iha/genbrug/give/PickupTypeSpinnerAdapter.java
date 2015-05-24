package com.iha.genbrug.give;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iha.genbrug.R;

import java.util.List;

/**
 * Created by Gladiator HelmetFace on 5/16/2015.
 */
public class PickupTypeSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> objects;

    public PickupTypeSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate(R.layout.pickup_type_dropdown_item_layout, parent, false);
        TextView label = (TextView)row.findViewById(R.id.tv_pickup_type_name);
        label.setText(objects.get(position));

        ImageView icon=(ImageView)row.findViewById(R.id.iv_pickup_type_icon);

        if(position == getCount())
            icon.setImageResource(R.drawable.ic_action_dropdown);
        else
            icon.setImageResource(R.drawable.ic_action_camera);

        return row;
    }
}
