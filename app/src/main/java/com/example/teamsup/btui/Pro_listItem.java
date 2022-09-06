package com.example.teamsup.btui;

public class Pro_listItem implements Comparable<Pro_listItem>{
    //프로필화면과 프로젝트방 검색화면의 프로젝트방 리스트에 사용되는 아이템클래스
    String num;
    String name;

    public Pro_listItem(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Pro_listItem pro_listItem) {
        //정렬순서 : 아이템의 숫자 순서
        return this.num.compareTo(pro_listItem.num);
    }
}
