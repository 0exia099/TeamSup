package com.example.teamsup.ui.teammember;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.teamsup.MainActivity;
import com.example.teamsup.Profile;
import com.example.teamsup.ProjectRoom;
import com.example.teamsup.databinding.FragmentTeammemberBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamFragment extends Fragment {
    //팀원 화면을 나타내는 프래그먼트
    private ListView listview = null;
    private TeamListViewAdapter adapter = null;

    private FragmentTeammemberBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeamViewModel teamViewModel =
                new ViewModelProvider(this).get(TeamViewModel.class);

        binding = FragmentTeammemberBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.listview = binding.teamListview;
        adapter = new TeamListViewAdapter();
        ProjectRoom pr = ((MainActivity) MainActivity.context_main).pr;

        FirebaseDatabase.getInstance().getReference("PR").child(pr.getHostEmail()).child(pr.getRoomName()).child("teamMember").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String tname = postSnapshot.child("name").getValue().toString();
                    String temail = postSnapshot.getKey().toString();
                    String tphone = postSnapshot.child("phoneNum").getValue().toString();
                    Boolean tiso = (Boolean) postSnapshot.child("isolation").getValue();
                    Profile profile = new Profile(tname, temail, tphone);
                    profile.setIsolation(tiso);
                    adapter.addItem(new Team_listItem(profile));
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