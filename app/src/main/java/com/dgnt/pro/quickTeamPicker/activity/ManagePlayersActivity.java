package com.dgnt.pro.quickTeamPicker.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.dgnt.pro.quickTeamPicker.R;
import com.dgnt.pro.quickTeamPicker.adapter.PlayerExpandableListAdapter;
import com.dgnt.pro.quickTeamPicker.holder.Group;
import com.dgnt.pro.quickTeamPicker.holder.Player;
import com.dgnt.pro.quickTeamPicker.util.DatabaseHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagePlayersActivity extends AppCompatActivity {

    private final DatabaseHelper dh = new DatabaseHelper(this);
    private TextView noPlayersMsg_tv;
    private ExpandableListView players_elv;
    private PlayerExpandableListAdapter playerListAdapter;
    private List<Group> groupList;
    private HashMap<String, List<Player>> groupPlayerMap;
    private Set<String> expandedGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        groupPlayerMap = new HashMap<>();
        groupList = dh.getAllGroups();
        for (final Group group : groupList) {
            groupPlayerMap.put(group.getName(), group.getPlayerList());
        }

        playerListAdapter = new PlayerExpandableListAdapter(this, groupList, groupPlayerMap);
        players_elv = (ExpandableListView) findViewById(R.id.players_elv);
        players_elv.setAdapter(playerListAdapter);

        players_elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, final View v,
                                        final int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        players_elv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                return false;
            }
        });

        expandedGroups = new HashSet<>();
        players_elv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                expandedGroups.remove(groupList.get(groupPosition).getName());

            }
        });

        players_elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                expandedGroups.add(groupList.get(groupPosition).getName());
            }
        });
        for (int i = 0; i < playerListAdapter.getGroupCount(); i++) {
            expandedGroups.add(groupList.get(i).getName());
            players_elv.expandGroup(i);
        }

        noPlayersMsg_tv = (TextView) findViewById(R.id.noPlayersMsg_tv);
        noPlayersMsg_tv.setVisibility(groupList.size() == 0 ? View.VISIBLE : View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showAddPlayerDialog(view.getContext());
            }
        });


    }

    private void updateList() {
        updateList(null);
    }
    private void updateList(final String newGroup) {

        if (newGroup!=null && !groupPlayerMap.containsKey(newGroup) ){
            expandedGroups.add(newGroup);
        }

        groupPlayerMap.clear();
        groupList.clear();

        final List<Group> groupListFromDB = dh.getAllGroups();
        for (final Group group : groupListFromDB) {
            groupList.add(group);
            groupPlayerMap.put(group.getName(), group.getPlayerList());
        }

        for (int i = 0; i<groupList.size(); i++){
            if (expandedGroups.contains(groupList.get(i).getName()))
                players_elv.expandGroup(i);
            else
                players_elv.collapseGroup(i);
        }

        playerListAdapter.notifyDataSetChanged();


        noPlayersMsg_tv.setVisibility(groupList.size() == 0 ? View.VISIBLE : View.GONE);

    }

    private void showAddPlayerDialog(final Context context) {
        final View layout_add_player = LayoutInflater.from(context).inflate(R.layout.layout_add_player, (LinearLayout) findViewById(R.id.layout_add_player_rootLayout));

        final EditText player_et = (EditText) layout_add_player.findViewById(R.id.player_et);
        final EditText skill_et = (EditText) layout_add_player.findViewById(R.id.skill_et);

        final AutoCompleteTextView group_actv = (AutoCompleteTextView) layout_add_player.findViewById(R.id.group_actv);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, groupPlayerMap.keySet().toArray(new String[groupPlayerMap.size()]));
        group_actv.setAdapter(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout_add_player);

        builder.setTitle(getResources().getString(R.string.addPlayerTitle));

        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                handleAddPlayer(player_et, skill_et, group_actv);
            }
        });
        builder.setNeutralButton(getString(R.string.addAndContinue), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                handleAddPlayer(player_et, skill_et, group_actv);
                showAddPlayerDialog(context);
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void handleAddPlayer(final EditText player_et, final EditText skill_et, final AutoCompleteTextView group_actv) {
        final String name = player_et.getText().toString();
        if (name.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.playerNameEmptyErrorMsg), Toast.LENGTH_LONG).show();

            return;
        }

        String groupId = group_actv.getText().toString();
        if (groupId.trim().length() == 0) {
            groupId = getResources().getString(R.string.defaultGroupName);
        }

        int skill = 100;
        try {
            skill = Integer.parseInt(skill_et.getText().toString());
            if (skill < 0) {
                skill = 0;
            } else if (skill > 100) {
                skill = 100;
            }
        } catch (NumberFormatException e) {

        }


        final DatabaseHelper.Status status = dh.addPlayer(name, skill, groupId);
        switch (status) {
            case SUCCESS:
                updateList(groupId);
                break;
            case CONSTRAINT:
                Toast.makeText(getApplicationContext(), getString(R.string.duplicatePlayerName, name), Toast.LENGTH_LONG).show();

                break;
            case FAIL:
                Toast.makeText(getApplicationContext(), getString(R.string.unexpectedErrorMsg, name), Toast.LENGTH_LONG).show();

                break;
        }
    }

}
