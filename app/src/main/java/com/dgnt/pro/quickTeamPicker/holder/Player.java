package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 10/7/2015.
 * used http://www.parcelabler.com/ to make this class parcelable
 */
public class Player implements Parcelable {

    private String name;
    private int skill;
    private String groupId;

    public Player(final String name, final int skill, final String groupId){
        this.name = name;
        this.skill = skill;
        this.groupId = groupId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String toString()
    {
        return getName();
    }
    protected Player(Parcel in) {
        name = in.readString();
        skill = in.readInt();
        groupId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(skill);
        dest.writeString(groupId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
