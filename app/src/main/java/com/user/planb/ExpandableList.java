package com.user.planb;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/22/2018.
 */

public class ExpandableList extends BaseExpandableListAdapter {


    private Context _context;
    private List<Integer> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<Integer>> _listDataChild;
    Map<Integer, String> states = new HashMap<Integer, String>();
    Map<Integer,Map<Integer,String>> districts = new HashMap<>();


    public ExpandableList(Context context, List<Integer> listDataHeader,Map<Integer, String> states,
                                 HashMap<Integer,List<Integer>> listChildData,Map<Integer,Map<Integer,String>> districts) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.states = states;
        this.districts = districts;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        Integer get_dei_is = this._listDataHeader.get(groupPosition);
        Map<Integer,String> dis_id = districts.get(get_dei_is);
        List<Integer> dis = this._listDataChild.get(get_dei_is);
        Integer dis_pos = dis.get(childPosititon);
        return dis_id.get(dis_pos);
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        //.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        Integer state_id = this._listDataHeader.get(groupPosition);
        List<Integer> districts_id = _listDataChild.get(state_id);
        Log.e("child count is","child count "+ _listDataChild+ " "+state_id);
        return districts_id.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        Integer state_id = this._listDataHeader.get(groupPosition);
        return states.get(state_id);
    }

    @Override
    public int getGroupCount() {

        Log.e("degugger011","debugger011 "+this._listDataHeader.size());
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
}
