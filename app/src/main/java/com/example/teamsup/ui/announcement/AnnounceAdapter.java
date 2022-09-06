package com.example.teamsup.ui.announcement;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamsup.Message;
import com.example.teamsup.R;

import java.util.ArrayList;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.MyViewHolder>{
    //공지사항 화면에서 공지사항리스트의 어댑터 클래스
    private ArrayList<Message> mDataset;
    String name;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView msgText;
        public TextView timeText;
        public View rootView;
        public LinearLayout msgLinear;
        public MyViewHolder(View v){
            super(v);
            nameText = v.findViewById(R.id.AnameText);
            msgText = v.findViewById(R.id.announceM);
            timeText = v.findViewById(R.id.AtimeText);
            msgLinear = v.findViewById(R.id.announceLinear);
            rootView = v;
        }
    }
    public AnnounceAdapter(ArrayList<Message> mDataset, String name) {
        this.mDataset = mDataset;
        this.name = name;
    }

    @NonNull
    @Override
    public AnnounceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.announce_text_view, parent, false);

        AnnounceAdapter.MyViewHolder viewholder = new AnnounceAdapter.MyViewHolder(linearLayout);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message msg = mDataset.get(position);
        holder.nameText.setText(msg.getSender());
        holder.msgText.setText(msg.getMsg());
        holder.timeText.setText(msg.getTime());
        holder.msgLinear.setGravity(Gravity.CENTER);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setArrayData(Message MData) {
        mDataset.add(MData);
        notifyItemInserted(mDataset.size()-1);
    }
}
