package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 10/7/2015.
 * used http://www.parcelabler.com/ to make this class parcelable
 */
public class Person implements Parcelable {

    private long id;
    private String name;
    private int skill;
    private long groupId;

    public Person ( final long id, final String name, final int skill, final long groupId){
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.groupId = groupId;
    }

    public long getId() {
        return id;
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

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    protected Person(Parcel in) {
        id = in.readLong();
        name = in.readString();
        skill = in.readInt();
        groupId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(skill);
        dest.writeLong(groupId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
