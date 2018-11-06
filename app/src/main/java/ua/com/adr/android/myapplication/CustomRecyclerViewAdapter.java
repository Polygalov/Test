package ua.com.adr.android.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.com.adr.android.myapplication.db.Hyperlinks;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    private List<Hyperlinks> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    CustomRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // String animal = mData.get(position);
        holder.bind(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUrlAdress, tvTime;
        LinearLayout linLayRvRow;

        ViewHolder(View itemView) {
            super(itemView);
            tvUrlAdress = itemView.findViewById(R.id.tvUrlAdress);
            tvTime = itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);
        }

        void bind(final Hyperlinks links) {
            if (links != null) {
                //creating Date from millisecond
                Date linkDate = new Date(links.getTime());

                tvUrlAdress.setText(links.getUrl());
                tvTime.setText(new SimpleDateFormat("dd-MM-YY HH:mm:ss").format(linkDate));
                itemView.setBackgroundColor(colorSelector(links.getStatus()));
            }
        }

        private int colorSelector(int status) {
            int backColor;
            switch (status) {
                case 1:  backColor = Color.GREEN;
                    break;
                case 2:  backColor = Color.RED;
                    break;
                case 3:  backColor = Color.GRAY;
                    break;
                default: backColor = Color.GRAY;
                    break;
            }
            return backColor;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setData(List<Hyperlinks> newData) {
        if (mData != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(mData, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            mData.clear();
            mData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            mData = newData;
        }
    }

    String getItemUrl(int id) {
        return mData.get(id).getUrl();
    }

    int getItemStatus(int id) {
        return mData.get(id).getStatus();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Hyperlinks> oldPosts, newPosts;

        public PostDiffCallback(List<Hyperlinks> oldPosts, List<Hyperlinks> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).getId() == newPosts.get(newItemPosition).getId();
           // return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }
}
