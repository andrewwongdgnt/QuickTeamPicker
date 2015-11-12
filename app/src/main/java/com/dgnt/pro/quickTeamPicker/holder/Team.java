package com.dgnt.pro.quickTeamPicker.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Andrew on 10/7/2015.
 */
public class Team extends Group implements Parcelable {

    private boolean dirty;
    private int skillSum;

    public Team(final String name, final List<Player> playerList){
        super(name, playerList);
        dirty=true;
    }

    public List<Player> getPlayerList() {
        dirty=true;
        return super.getPlayerList();
    }

    public void setPlayerList(List<Player> playerList) {
        dirty=true;
        super.setPlayerList(playerList);
    }

    public boolean addPlayer(Player player) {
        dirty=super.addPlayer(player);
        return dirty;
    }

    public boolean removePlayer(Player player) {
        dirty=super.removePlayer(player);
        return dirty;
    }

    public int getSkillSum(){
        if (dirty){
            skillSum =0;
            for (Player p : getPlayerList()){
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
