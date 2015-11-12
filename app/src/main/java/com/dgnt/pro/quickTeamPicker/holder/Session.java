package com.dgnt.pro.quickTeamPicker.holder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/12/2015.
 */
public class Session {

    private long id;
    private String name;
    private List<Team> teamList;
    private long timeCreated;
    private long timeModified;

    public Session(final long id, final String name, final List<Team> teamList, final long timeCreated, final long timeModified) {
        this.id = id;
        this.name = name;
        this.teamList = teamList;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
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

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(long timeModified) {
        this.timeModified = timeModified;
    }

    public static String teamListToJson(final List<Team> teamList) {
        try {
            final JSONArray jsonArray = new JSONArray();

            for (final Team team : teamList) {

                final JSONObject object = new JSONObject();
                object.put("name", team.getName());

                final JSONArray playerJsonArray = new JSONArray();

                for (final Player player : team.getPlayerList()) {

                    final JSONObject playerJsonObject = new JSONObject();
                    playerJsonObject.put("name", player.getName());
                    playerJsonObject.put("skill", player.getSkill());
                    playerJsonObject.put("groupId", player.getGroupId());

                    playerJsonArray.put(object);
                }

                object.put("playerList", playerJsonArray);

                jsonArray.put(object);

            }
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static List<Team> fromJsonToTeamList(final String jsonString) {


        try {
            final List<Team> teamList = new ArrayList<>();

            final JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                // Pulling items from the array
                final String name = object.getString("name");

                final JSONArray playerJsonArray = object.getJSONArray("playerList");
                final List<Player> playerList = new ArrayList<>();

                for (int j = 0; j < playerJsonArray.length(); j++) {
                    JSONObject playerJsonObject = jsonArray.getJSONObject(j);
                    // Pulling items from the array
                    final String playerName = playerJsonObject.getString("name");
                    final int playerSkill = playerJsonObject.getInt("skill");
                    final String playerGroupId = playerJsonObject.getString("groupId");

                    playerList.add(new Player(playerName, playerSkill, playerGroupId));
                }

                teamList.add(new Team(name, playerList));
            }
            return teamList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
