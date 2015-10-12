package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/7/2015.
 */
public class Group implements Parcelable {
    protected long id;
    protected String name;
    protected List<Person> personList;

    public Group(final long id, final String name, final List<Person> personList) {
        this.id = id;
        this.name = name;
        this.personList = personList;
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

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public boolean addPerson(final Person personToAdd) {
        for (final Person person : personList) {
            if (person.getId() == personToAdd.getId())
                return false;
        }
        personList.add(personToAdd);
        return true;
    }

    public boolean removePerson(final Person personToRemove) {
        for (int i = personList.size() - 1; i >= 0; i--) {
            if (personList.get(i).getId() == personToRemove.getId()) {
                personList.remove(i);
                return true;
            }
        }
        return false;
    }


    protected Group(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            personList = new ArrayList<Person>();
            in.readList(personList, Person.class.getClassLoader());
        } else {
            personList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (personList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(personList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
