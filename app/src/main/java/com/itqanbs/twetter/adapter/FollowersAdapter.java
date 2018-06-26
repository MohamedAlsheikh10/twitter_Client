package com.itqanbs.twetter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.model.Follower;

import java.util.List;


/**
 * Created by User on 5/29/2017.
 */


public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowersAdapterViewHolder> {
    Context context;
    List<Follower> FollowersAdapteralbumlist;


    Gson gson = new Gson();


    public FollowersAdapter(Context context, List<Follower> FollowersAdapteralbumlist) {
        this.context = context;
        this.FollowersAdapteralbumlist = FollowersAdapteralbumlist;

    }

    @Override
    public FollowersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followers_list_item, parent, false);
        return new FollowersAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowersAdapterViewHolder holder, final int position) {
        final Follower FollowersAdapterAlbum = FollowersAdapteralbumlist.get(position);

        Glide.with(context).
                load(FollowersAdapterAlbum.getProfilePictureUrl())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_box_black_24dp))
                .into(holder.follower_image);
        holder.follower_name.setText(FollowersAdapterAlbum.getName());
        holder.follower_bio.setText(FollowersAdapterAlbum.getDescription());

        holder.courseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return FollowersAdapteralbumlist.size();
    }

//    public void addAll(ArrayList<FollowersAdapter> FollowersAdapter) {
//        for (FollowersAdapter result : FollowersAdapter) {
//            add(result);
//        }
//    }

    public void add(Follower Course) {
        FollowersAdapteralbumlist.add(Course);
        notifyItemInserted(FollowersAdapteralbumlist.size() - 1);
    }

    private void addToFavoritList(FollowersAdapter coursemodel) {

//        MyApplication.FOLLOWERS_session_editor.putString("FAVORITELIST", gson.toJson(favorite_course));
//        MyApplication.FOLLOWERS_session_editor.putString("FAVORITELIST_ID", this.gson.toJson(favorite_course_id));
        MyApplication.FOLLOWERS_session_editor.commit();

    }


    class FollowersAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView follower_name;
        TextView follower_bio;
        ImageView follower_image;
        View courseItem;

        FollowersAdapterViewHolder(View itemView) {
            super(itemView);
            courseItem = itemView;
            follower_image = itemView.findViewById(R.id.follower_image);

            follower_name = itemView.findViewById(R.id.follower_name);
            follower_bio = itemView.findViewById(R.id.follower_bio);


        }
    }
}
