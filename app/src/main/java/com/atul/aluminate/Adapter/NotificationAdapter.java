package com.atul.aluminate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.atul.aluminate.views.CommentActivity;
import com.atul.aluminate.Model.Notification;
import com.atul.aluminate.Model.User;
import com.atul.aluminate.R;
import com.atul.aluminate.databinding.NotificationRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification notification = list.get(position);

        String type = notification.getType();

        //Get exact time
        String time = TimeAgo.using(notification.getNotificationAt());
        holder.binding.time.setText(time);

        //Getting User data from database based on NotificationBy
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        //Set user profile
                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.notificationProfile);

                        //Set notification text according to notification type
                        if (type.equals("like")){
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName() +"</b>"+ " liked your post"));
                        }else if (type.equals("comment")){
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName() +"</b>"+ " Commented on your post"));
                        }else {
                            holder.binding.notification.setText(Html.fromHtml("<b>" +user.getName()+"</b>" + " start following you."));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Open Notification
        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("follow")){
                    //Set checkOpen to true when user open notification
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(notification.getPostedBy())
                            .child(notification.getNotificationID())
                            .child("checkOpen")
                            .setValue(true);
                    //change notification background color
                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    //open comment activity
                    Intent intent = new Intent(context, CommentActivity.class);
                    //Send data to Comment activity through Intent
                    intent.putExtra("postId", notification.getPostID());
                    intent.putExtra("postedBy", notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
        //Check if user already opened this notification or not
        Boolean checkOpen = notification.isCheckOpen();
        if (checkOpen == true){
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        //Binding
        NotificationRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationRvDesignBinding.bind(itemView);
        }
    }
}
