package chahyunbin.cwapp1.Video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import chahyunbin.cwapp1.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context mContext;
    private List<UploadImage> mUploadImages;
    private OnItemClickListener mListener;

    public VideoAdapter(Context mContext, List<UploadImage> mUploadImages) {
        this.mContext = mContext;
        this.mUploadImages = mUploadImages;
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        UploadImage uploadCurrent = mUploadImages.get(position);
        holder.textViewName.setText(uploadCurrent.getmName());
        Picasso.with(mContext)
                .load(uploadCurrent.getmImageUri())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploadImages.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;

        public VideoViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem download = menu.add(Menu.NONE, 1, 1, "내 폰에 저장");

            download.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                    switch (item.getItemId()){
                        case 1 :
                            mListener.onDownloadClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDownloadClick(int position);


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
