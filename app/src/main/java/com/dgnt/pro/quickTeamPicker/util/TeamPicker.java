package com.dgnt.pro.quickTeamPicker.util;

import com.dgnt.pro.quickTeamPicker.holder.Player;
import com.dgnt.pro.quickTeamPicker.holder.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/8/2015.
 */
public class TeamPicker {
    public static List<Team> pickTeams(final List<Player> playerList, final int numTeams, final boolean balanced) {
        final List<Team> teamList = new ArrayList<>();

        if (balanced) {
            final int[] mappedSkillList = new int[playerList.size()];
            for (int i = 0; i< playerList.size(); i++){
                mappedSkillList[i] = playerList.get(i).getSkill();
            }

            final int[] dividers = PartitionUtil.solveDynamicProgrammatically(mappedSkillList, numTeams);

            int start = 0;
            int teamNumber = 1;
            for (int d : dividers) {
                teamList.add(new Team("team "+teamNumber, playerList.subList(start, d)));
                start = d;
                teamNumber++;
            }
            ;
            teamList.add(new Team("team " + teamNumber,  playerList.subList(start, playerList.size())));


        }

        return teamList;
    }
}
