package com.example.teamsup.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.teamsup.MainActivity;
import com.example.teamsup.ProjectRoom;
import com.example.teamsup.SetscheduleActivity;
import com.example.teamsup.databinding.FragmentScheduleBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleFragment extends Fragment {
    //일정화면을 나타내는 프래그먼트

    private ListView listview = null;
    private Button setSchedule = null;
    private ScheduleListViewAdapter adapter = null;
    private FragmentScheduleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScheduleViewModel scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.listview = binding.scheduleListview;
        this.setSchedule = binding.scheduleSet;
        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), SetscheduleActivity.class);
                in.putExtra("account", ((MainActivity)MainActivity.context_main).account);
                in.putExtra("user", ((MainActivity)MainActivity.context_main).user);
                in.putExtra("pr", ((MainActivity)MainActivity.context_main).pr);
                startActivity(in);
            }
        });

        ProjectRoom pr = ((MainActivity) MainActivity.context_main).pr;

        FirebaseDatabase.getInstance().getReference("PR").child(pr.getHostEmail()).child(pr.getRoomName()).child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter = new ScheduleListViewAdapter();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String msg = postSnapshot.getValue().toString();
                    adapter.addItem(new Schedule_listItem(msg));
                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("app", "Failed to read value.", error.toException());
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}