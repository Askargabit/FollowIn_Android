package followininc.followin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import followininc.followin.Backend.ImageLoader;

public class RelationShipAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> usersInfo;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public RelationShipAdapter(Context context, ArrayList<HashMap<String, String>> usersInfo) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.usersInfo = usersInfo;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.relationship_inflater, null);
		Holder holder = new Holder();
		holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
		holder.tvFullName = (TextView) view.findViewById(R.id.tvFullName);
		holder.tvAddInfo = (TextView) view.findViewById(R.id.tvAddInfo);
		holder.tvFullName.setText(usersInfo.get(position).get("username"));
		holder.tvAddInfo.setText(usersInfo.get(position).get("Info"));
		imageLoader.DisplayImage(usersInfo.get(position).get("profile_picture"), holder.ivPhoto);
		return view;
	}

	public void clearData() {
		// clear the data
		usersInfo.clear();
	}

	private class Holder {
		private ImageView ivPhoto;
		private TextView tvFullName;
		private TextView tvAddInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return usersInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
