package com.pratap.endlessrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;

	private List<GalleryPojo> studentList;

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 5;
	private int lastVisibleItem, totalItemCount;
	private boolean loading;
	private OnLoadMoreListener onLoadMoreListener;
	private Context context;

	

	public DataAdapter(List<GalleryPojo> students, RecyclerView recyclerView) {
		studentList = students;

		if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

			final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
					.getLayoutManager();


					recyclerView
					.addOnScrollListener(new RecyclerView.OnScrollListener() {
						@Override
						public void onScrolled(RecyclerView recyclerView,
											   int dx, int dy) {
							super.onScrolled(recyclerView, dx, dy);

							totalItemCount = linearLayoutManager.getItemCount();
							lastVisibleItem = linearLayoutManager
									.findLastVisibleItemPosition();
							if (!loading
									&& totalItemCount <= (lastVisibleItem + visibleThreshold)) {
								// End has been reached
								// Do something
								if (onLoadMoreListener != null) {
									onLoadMoreListener.onLoadMore();
								}
								loading = true;
							}
						}
					});
		}
	}

	@Override
	public int getItemViewType(int position) {
		return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		RecyclerView.ViewHolder vh;
		if (viewType == VIEW_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.single_gallery_image, parent, false);

			vh = new StudentViewHolder(v);
		} else {
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.progress_item, parent, false);

			vh = new ProgressViewHolder(v);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof StudentViewHolder) {

			GalleryPojo singleStudent= (GalleryPojo) studentList.get(position);
			
			((StudentViewHolder) holder).etUploaderName.setText(singleStudent.getUploaded_by());
			
			((StudentViewHolder) holder).etDate_Time.setText(singleStudent.getDate_time());
			((StudentViewHolder) holder).comment.setText(singleStudent.getImageComment());

			((StudentViewHolder) holder).student= singleStudent;

			/*byte[] decodedString = Base64.decode(singleStudent.getImage(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

			((StudentViewHolder) holder).imageGallery.setImageBitmap(decodedByte);*/

			/*Glide.with(context)
					.load(Base64.decode(singleStudent.getImage(), Base64.DEFAULT))
					.placeholder(R.drawable.ic_camera)
					.into(((StudentViewHolder) holder).imageGallery);*/
			
		} else {
			((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
		}
	}

	public void setLoaded() {
		loading = false;
	}

	@Override
	public int getItemCount() {
		return studentList.size();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.onLoadMoreListener = onLoadMoreListener;
	}


	//
	public static class StudentViewHolder extends RecyclerView.ViewHolder {
		TextView comment, etUploaderName, etDate_Time;
		CardView cv;
		ImageView imageGallery;
		public GalleryPojo student;


		public StudentViewHolder(View v) {
			super(v);
			cv = (CardView) itemView.findViewById(R.id.card_view);
			comment = (TextView) itemView.findViewById(R.id.tvComment);
			etUploaderName = (TextView) itemView.findViewById(R.id.etUploaderName);
			etDate_Time = (TextView) itemView.findViewById(R.id.etDate_Time);
			imageGallery = (ImageView) itemView.findViewById(R.id.imageGallery);

		}
	}

	public static class ProgressViewHolder extends RecyclerView.ViewHolder {
		public ProgressBar progressBar;

		public ProgressViewHolder(View v) {
			super(v);
			progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
		}
	}
}