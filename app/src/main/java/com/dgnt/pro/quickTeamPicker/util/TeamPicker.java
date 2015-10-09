package com.dgnt.pro.quickTeamPicker.util;

import com.dgnt.pro.quickTeamPicker.holder.Person;
import com.dgnt.pro.quickTeamPicker.holder.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrew on 10/8/2015.
 */
public class TeamPicker {
    public static List<Team> pickTeams(final List<Person> personList, final int numTeams, final boolean balanced) {
        final List<Team> teamList = new ArrayList<>();

        if (balanced) {
            final int[] mappedSkillList = new int[personList.size()];
            for (int i = 0; i<personList.size(); i++){
                mappedSkillList[i] = personList.get(i).getSkill();
            }

            final int[] dividers = PartitionUtil.solveDynamicProgrammatically(mappedSkillList, numTeams);

            int start = 0;
            int teamNumber = 1;
            for (int d : dividers) {
                teamList.add(new Team("team "+teamNumber, personList.subList(start, d)));
                start = d;
                teamNumber++;
            }
            ;
            teamList.add(new Team("team " + teamNumber,  personList.subList(start, personList.size())));


        }

        return teamList;
    }
}
