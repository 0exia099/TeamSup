package com.example.teamsup.ui.chatting;

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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //채팅화면의 채팅리스트의 어댑터
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
            nameText = v.findViewById(R.id.nameText);
            msgText = v.findViewById(R.id.chatM);
            timeText = v.findViewById(R.id.timeText);
            msgLinear = v.findViewById(R.id.msgLinear);
            rootView = v;
        }
    }
    public MyAdapter(ArrayList<Message> mDataset, String name) {
        this.mDataset = mDataset;
        this.name = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        MyViewHolder viewholder = new MyViewHolder(linearLayout);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message msg = mDataset.get(position);
        holder.nameText.setText(msg.getSender());
        holder.msgText.setText(msg.getMsg());
        holder.timeText.setText(msg.getTime());
        if(msg.getSender().equals(name)){
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.timeText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgLinear.setGravity(Gravity.RIGHT);
            holder.msgText.setBackgroundResource(R.drawable.my_msg);
        }
        else{
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.timeText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgLinear.setGravity(Gravity.LEFT);
            holder.msgText.setBackgroundResource(R.drawable.team_msg);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 데이터를 입력
    public void setArrayData(Message MData) {
        mDataset.add(MData);
        notifyItemInserted(mDataset.size()-1);
    }
}
