package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/7/2015.
 */
public class Group implements Parcelable {
    protected String name;
    protected List<Player> playerList;

    public Group(final String name, final List<Player> playerList) {
        this.name = name;
        this.playerList = playerList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public boolean addPlayer(final Player playerToAdd) {
        for (final Player player : playerList) {
            if (player.getName() == playerToAdd.getName())
                return false;
        }
        playerList.add(playerToAdd);
        return true;
    }

    public boolean removePlayer(final Player playerToRemove) {
        for (int i = playerList.size() - 1; i >= 0; i--) {
            if (playerList.get(i).getName() == playerToRemove.getName()) {
                playerList.remove(i);
                return true;
            }
        }
        return false;
    }


    protected Group(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            playerList = new ArrayList<>();
            in.readList(playerList, Player.class.getClassLoader());
        } else {
            playerList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (playerList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(playerList);
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
