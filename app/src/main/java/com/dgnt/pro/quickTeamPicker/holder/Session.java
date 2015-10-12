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
                object.put("id", team.getId());
                object.put("name", team.getName());

                final JSONArray personJsonArray = new JSONArray();

                for (final Person person : team.getPersonList()) {

                    final JSONObject personJsonObject = new JSONObject();
                    personJsonObject.put("id", person.getId());
                    personJsonObject.put("name", person.getName());
                    personJsonObject.put("skill", person.getSkill());
                    personJsonObject.put("groupId", person.getGroupId());

                    personJsonArray.put(object);
                }

                object.put("personList", personJsonArray);

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
                final long id = object.getLong("id");
                final String name = object.getString("name");

                final JSONArray personJsonArray = object.getJSONArray("personList");
                final List<Person> personList = new ArrayList<>();

                for (int j = 0; j < personJsonArray.length(); j++) {
                    JSONObject personJsonObject = jsonArray.getJSONObject(j);
                    // Pulling items from the array
                    final long personId = personJsonObject.getLong("id");
                    final String personName = personJsonObject.getString("name");
                    final int personSkill = personJsonObject.getInt("skill");
                    final long personGroupId = personJsonObject.getLong("groupId");

                    personList.add(new Person(personId, personName, personSkill, personGroupId));
                }

                teamList.add(new Team(id, name, personList));
            }
            return teamList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
