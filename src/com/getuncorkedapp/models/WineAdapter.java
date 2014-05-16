package com.getuncorkedapp.models;


import java.util.List;

import com.getuncorkedapp.R;
import com.getuncorkedapp.utils.WebImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WineAdapter extends ArrayAdapter<Wine> {

	private final Context context;
	private final List<Wine> wines;
	
	public WineAdapter(Context context, List<Wine> wines) {
		super(context, R.layout.row_wine_list, wines);
		this.context = context;
		this.wines = wines;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_wine_list, parent, false);

        Wine wine = wines.get(position);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView winary = (TextView) rowView.findViewById(R.id.winary);
        TextView type = (TextView) rowView.findViewById(R.id.type);
        WebImageView icon = (WebImageView) rowView.findViewById(R.id.icon);

        name.setText( wine.getName() );
        winary.setText( wine.getWinary() );
        type.setText( wine.getType() );
        icon.setImageUrl( wine.getImageFile().getUrl() );
        Log.i("WineAdapter", wine.getObjectId() );

        return rowView;

    }
}
