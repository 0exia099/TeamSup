package com.example.teamsup.ui.announcement;

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
import com.example.teamsup.AnnouncementRoom;
import com.example.teamsup.MainActivity;
import com.example.teamsup.Message;
import com.example.teamsup.ProjectRoom;
import com.example.teamsup.User;
import com.example.teamsup.databinding.FragmentAnnouncementBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnnouncementFragment extends Fragment {
    //공지사항 화면 프래그먼트

    private FragmentAnnouncementBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String name;
    private EditText announceText;
    private Button createButton;

    Account account;
    User user;
    ProjectRoom pr;
    Thread thread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AnnouncementViewModel announcementViewModel =
                new ViewModelProvider(this).get(AnnouncementViewModel.class);

        binding = FragmentAnnouncementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = (RecyclerView) binding.announceRecyceler;

        account = ((MainActivity) MainActivity.context_main).account;
        user = ((MainActivity) MainActivity.context_main).user;
        pr = ((MainActivity) MainActivity.context_main).pr;

        AnnouncementRoom announcementRoom = pr.getAR();

        name = account.getProfile().getName();
        announceText = binding.announceText;
        createButton = binding.AsendButton;
        createButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //보내기 버튼이 클릭되면 edittext에 작성한 공지사항을 AnnouncementRoom의 sendMessage메소드를 통해 파이어베이스에 기록
                String msg = announceText.getText().toString();
                if(msg != null){
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    Message message = new Message(name, msg, sendTime);
                    announceText.setText("");
                    announcementRoom.sendMessage(message);
                }
            }
        });

        ((MainActivity) MainActivity.context_main).announcementView = recyclerView;
        //쓰레드로 AnnouncementRoom의 viewAnnounce를 실행시켜 계속해서 공지사항 갱신
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                announcementRoom.viewAnnounce();
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