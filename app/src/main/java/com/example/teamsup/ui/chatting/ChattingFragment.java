package com.example.teamsup.ui.chatting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamsup.Account;
import com.example.teamsup.ChatRoom;
import com.example.teamsup.MainActivity;
import com.example.teamsup.Message;
import com.example.teamsup.ProjectRoom;
import com.example.teamsup.User;
import com.example.teamsup.databinding.FragmentChattingBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChattingFragment extends Fragment {
    //채팅화면 프래그먼트

    private FragmentChattingBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String name;
    private EditText chatText;
    private Button sendButton;
    private DatabaseReference myRef;
    Account account;
    User user;
    ProjectRoom pr;
    Thread thread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChattingViewModel homeViewModel =
                new ViewModelProvider(this).get(ChattingViewModel.class);

        binding = FragmentChattingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = (RecyclerView) binding.chatRecyceler;

        account = ((MainActivity) MainActivity.context_main).account;
        user = ((MainActivity) MainActivity.context_main).user;
        pr = ((MainActivity) MainActivity.context_main).pr;

        ChatRoom chatRoom = pr.getCR();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("PR");

        name = account.getProfile().getName();
        chatText = binding.chatText;
        sendButton = binding.sendButton;
        sendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //보내기 버튼이 클릭되면 edittext에 작성한 채팅을 ChatRoom의 sendMessage메소드를 통해 파이어베이스에 기록
                String msg = chatText.getText().toString();
                if(msg != null){
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    Message message = new Message(name, msg, sendTime);
                    chatText.setText("");
                    chatRoom.sendMessage(message);

                }
            }
        });

        ((MainActivity) MainActivity.context_main).chatView = recyclerView;
        //쓰레드로 ChatRoom의 viewChat메소드 실행하여 계속하여 새로운 채팅확인
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chatRoom.viewChat();
            }
        });
        thread.start();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}