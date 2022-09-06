package com.example.teamsup.ui.teammember;

import com.example.teamsup.Profile;

public class Team_listItem implements Comparable<Team_listItem>{
    //팀 프로필 리스트의 아이템
    Profile team;

    public Team_listItem(Profile team) {
        this.team = team;
    }

    public void setTeam(Profile team) {
        this.team = team;
    }

    public Profile getTeam() {
        return team;
    }

    @Override
    public int compareTo(Team_listItem team_listItem) {
        return this.getTeam().getName().compareTo(team_listItem.getTeam().getName());
    }
}
