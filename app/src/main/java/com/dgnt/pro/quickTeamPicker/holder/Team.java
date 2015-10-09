package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/7/2015.
 */
public class Team extends Group implements Parcelable,IKeyable {

    private boolean dirty;
    private int skillSum;

    public Team(final String name, final List<Person> personList ){
        super(name,personList);
        dirty=true;
    }

    public List<Person> getPersonList() {
        dirty=true;
        return super.getPersonList();
    }

    public void setPersonList(List<Person> personList) {
        dirty=true;
        super.setPersonList(personList);
    }

    public boolean addPerson(Person person) {
        dirty=true;
        return super.addPerson(person);
    }

    public boolean removePerson(Person person) {
        dirty=true;
        return super.removePerson(person);
    }

    public int getSkillSum(){
        if (dirty){
            skillSum =0;
            for (Person p : getPersonList()){
                skillSum += p.getSkill();
            }
            dirty=false;
        }
        return skillSum;
    }

    protected Team(Parcel in) {
        super(in);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}
