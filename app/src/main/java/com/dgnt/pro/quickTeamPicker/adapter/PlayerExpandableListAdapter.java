package com.dgnt.pro.quickTeamPicker.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dgnt.pro.quickTeamPicker.R;
import com.dgnt.pro.quickTeamPicker.holder.Group;
import com.dgnt.pro.quickTeamPicker.holder.Player;
import com.dgnt.pro.quickTeamPicker.util.ConversionUtil;

import java.util.HashMap;
import java.util.List;

public class PlayerExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Group> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Player>> _listDataChild;

    public PlayerExpandableListAdapter(Context context, List<Group> listDataHeader,
                                       HashMap<String, List<Player>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Player getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getName()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Player player = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.player_list_item, null);
        }

        final TextView playerListItem_tv = (TextView) convertView.findViewById(R.id.playerListItem_tv);
        playerListItem_tv.setText(player.getName());

        final TextView playerSkillListItem_tv = (TextView) convertView.findViewById(R.id.playerSkillListItem_tv);
        playerSkillListItem_tv.setText(Integer.toString(player.getSkill()));


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getName()).size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Group group = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.player_group_item, null);
        }

        final TextView playerGroupItem_tv = (TextView) convertView.findViewById(R.id.playerGroupItem_tv);
        playerGroupItem_tv.setText(group.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }
}