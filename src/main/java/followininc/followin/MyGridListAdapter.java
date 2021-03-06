package followininc.followin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import followininc.followin.Backend.ImageLoader;

public class MyGridListAdapter extends BaseAdapter {
	// private Context context;
	private ArrayList<String> imageThumbList;
	private ArrayList<String> likeslist;
	private ArrayList<String> commentslist;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public MyGridListAdapter(Context context, ArrayList<String> imageThumbList) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageThumbList = imageThumbList;
		this.imageLoader = new ImageLoader(context);
	}


	public MyGridListAdapter(Context context, ArrayList<String> imageThumbList, ArrayList<String> likeslist, ArrayList<String> commentslist) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageThumbList = imageThumbList;
		this.likeslist = likeslist;
		this.commentslist = commentslist;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return imageThumbList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.media_list_inflater, null);
		Holder holder = new Holder();
		holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
		//holder.ivText = (TextView) view.findViewById(R.id.ivtextView);
		holder.ivLikeimage = (ImageView) view.findViewById(R.id.ivLikeimage);
		holder.ivCommentimage = (ImageView) view.findViewById(R.id.ivCommentimage);
		holder.ivLikes = (TextView) view.findViewById(R.id.ivLikes);
		holder.ivComments = (TextView) view.findViewById(R.id.ivComments);
		imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);
		if(commentslist != null && likeslist != null)
		{
			holder.ivLikes.setText(likeslist.get(position));
			holder.ivComments.setText(commentslist.get(position));
		}
		return view;
	}

	private class Holder {
		private ImageView ivPhoto;
		//private TextView ivText;
		private ImageView ivLikeimage;
		private ImageView ivCommentimage;
		private TextView ivLikes;
		private TextView ivComments;
	}

}
