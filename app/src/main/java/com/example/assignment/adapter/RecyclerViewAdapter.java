package com.example.assignment.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.UserInfo;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<UserInfo> userInfos;

    public void setFilteredList(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(Context context, List<UserInfo> userInfos) {
        this.context = context;
        this.userInfos = userInfos;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        UserInfo userInfo = userInfos.get(position);
        holder.firstName.setText(userInfo.getFirstName());
        holder.lastName.setText(userInfo.getLastName());
        holder.id.setText(Integer.toString(userInfo.getId()));
        holder.emailId.setText(userInfo.getEmailId());
        Picasso.get().load(userInfo.getAvatar()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView firstName;
        public TextView lastName;
        public TextView id;
        public TextView emailId;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            id = itemView.findViewById(R.id._id);
            emailId = itemView.findViewById(R.id.email_id);
            imageView = itemView.findViewById(R.id.imageView2);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageTask(ImageView imageView)
        {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap img = null;
            try
            {
                InputStream in = new java.net.URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result)
        {
            imageView.setImageBitmap(result);
        }
    }
}
