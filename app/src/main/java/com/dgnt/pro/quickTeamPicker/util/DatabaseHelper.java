package com.dgnt.pro.quickTeamPicker.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.dgnt.pro.quickTeamPicker.holder.Group;
import com.dgnt.pro.quickTeamPicker.holder.Player;
import com.dgnt.pro.quickTeamPicker.holder.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 10/11/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "db";

    private static final String PLAYER_TABLE = "playerTable";
    private static final String PLAYER_COLUMN_NAME = "name";
    private static final String PLAYER_COLUMN_SKILL = "skill";
    private static final String PLAYER_COLUMN_GROUP_ID = "groupId";

    private static final String GROUP_TABLE = "groupTable";
    private static final String GROUP_COLUMN_NAME = "name";

    private static final String SESSION_TABLE = "sessionTable";
    private static final String SESSION_COLUMN_ID = "id";
    private static final String SESSION_COLUMN_NAME = "name";
    private static final String SESSION_COLUMN_SERIALIZED_TEAM_LIST = "teamList";
    private static final String SESSION_COLUMN_TIME_CREATED = "timeCreated";
    private static final String SESSION_COLUMN_TIME_MODIFIED = "timeModified";

    //Status
    public enum Status {
        SUCCESS, CONSTRAINT, FAIL
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //Create Player Table
        final String createPlayerTableQuery = "CREATE TABLE " + PLAYER_TABLE + "(" +
                PLAYER_COLUMN_NAME + " TEXT PRIMARY KEY, " +
                PLAYER_COLUMN_SKILL + " INTEGER, " +
                PLAYER_COLUMN_GROUP_ID + " TEXT " +
                ")";

        db.execSQL(createPlayerTableQuery);

        //Create Group Table
        final String createGroupTableQuery = "CREATE TABLE " + GROUP_TABLE + "(" +
                GROUP_COLUMN_NAME + " TEXT PRIMARY KEY" +
                ")";

        db.execSQL(createGroupTableQuery);


        //Create Session Table
        final String createSessionTableQuery = "CREATE TABLE " + SESSION_TABLE + "(" +
                SESSION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SESSION_COLUMN_NAME + " TEXT, " +
                SESSION_COLUMN_SERIALIZED_TEAM_LIST + " TEXT, " +
                SESSION_COLUMN_TIME_CREATED + " INTEGER, " +
                SESSION_COLUMN_TIME_MODIFIED + " INTEGER " +
                ")";

        db.execSQL(createSessionTableQuery);

    }

    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);

        onCreate(db);
    }

    //------------------
    // Player
    //------------------

    public Status addPlayer(final String name, final int skill, final String groupId) {

        addGroup(groupId);

        final SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;

        //Inserting Player
        values = new ContentValues();
        values.put(PLAYER_COLUMN_NAME, name);
        values.put(PLAYER_COLUMN_SKILL, skill);
        values.put(PLAYER_COLUMN_GROUP_ID, groupId);
        try {
            db.insertOrThrow(PLAYER_TABLE, null, values);
            return Status.SUCCESS;
        } catch (SQLiteConstraintException e) {
            return Status.CONSTRAINT;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close(); // Closing database connection

        }
    }

    public Status updatePlayer(final String oldName, final String name, final int skill, final String groupId) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(PLAYER_COLUMN_NAME, name);
        values.put(PLAYER_COLUMN_SKILL, skill);
        values.put(PLAYER_COLUMN_GROUP_ID, groupId);


        // updating row
        try {
            db.update(PLAYER_TABLE, values, PLAYER_COLUMN_NAME + " = ?",
                    new String[]{oldName});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public Status deletePlayer(final String name) {
        final SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(PLAYER_TABLE, PLAYER_COLUMN_NAME + " = ?",
                    new String[]{name});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public Player getPlayer(final String name) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(PLAYER_TABLE, new String[]{
                        PLAYER_COLUMN_SKILL,
                        PLAYER_COLUMN_GROUP_ID
                }, PLAYER_COLUMN_NAME + "=?",
                new String[]{name}, null, null, null, null);

        Player player = null;

        if (cursor != null && cursor.moveToFirst()) {

            final int skill = cursor.getInt(cursor.getColumnIndex(PLAYER_COLUMN_SKILL));
            final String groupId = cursor.getString(cursor.getColumnIndex(PLAYER_COLUMN_GROUP_ID));


            player = new Player(name, skill, groupId);
        }

        cursor.close();
        db.close();

        return player;

    }


    //------------------
    // Groups
    //------------------
    public Status addGroup(final String name) {

        final SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;

        //Insert into group
        values = new ContentValues();
        values.put(GROUP_COLUMN_NAME, name);
        try {
            db.insertOrThrow(GROUP_TABLE, null, values);
            return Status.SUCCESS;
        } catch (SQLiteConstraintException e) {
            return Status.CONSTRAINT;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close(); // Closing database connection

        }


    }

    public Status updateGroup(final String oldName, final String name) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(GROUP_COLUMN_NAME, name);

        // updating row
        try {
            db.update(GROUP_TABLE, values, GROUP_COLUMN_NAME + " = ?",
                    new String[]{oldName});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public Status deleteGroup(final String oldName) {
        final SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(GROUP_TABLE, GROUP_COLUMN_NAME + " = ?",
                    new String[]{oldName});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public List<Group> getAllGroups() {
        final List<Group> groupList = new ArrayList<>();

        final SQLiteDatabase db = this.getReadableDatabase();

        final Map<String, List<Player>> groupMap = new HashMap<>();

        final Cursor cursor = db.query(PLAYER_TABLE, new String[]{
                        PLAYER_COLUMN_NAME,
                        PLAYER_COLUMN_SKILL,
                        PLAYER_COLUMN_GROUP_ID
                }, null,
                null, null, null, PLAYER_COLUMN_NAME + " ASC");


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {
                    final String name = cursor.getString(cursor.getColumnIndex(PLAYER_COLUMN_NAME));
                    final int skill = cursor.getInt(cursor.getColumnIndex(PLAYER_COLUMN_SKILL));
                    final String groupId = cursor.getString(cursor.getColumnIndex(PLAYER_COLUMN_GROUP_ID));


                    final Player player = new Player(name, skill, groupId);

                    if (!groupMap.containsKey(groupId))
                        groupMap.put(groupId, new ArrayList<Player>());

                    final List<Player> playerList = groupMap.get(groupId);
                    playerList.add(player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        final Cursor cursor2 = db.query(GROUP_TABLE, new String[]{
                        GROUP_COLUMN_NAME
                }, null,
                null, null, null, GROUP_COLUMN_NAME + " ASC");

        if (cursor2.moveToFirst()) {
            do {
                try {
                    final String name = cursor2.getString(cursor2.getColumnIndex(GROUP_COLUMN_NAME));

                    final Group group = new Group(name, groupMap.get(name));

                    groupList.add(group);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor2.moveToNext());
        }

        cursor.close();
        db.close();

        // return group list
        return groupList;

    }


    //------------------
    // Session
    //------------------

    public long addSession(final String name, final String serializedTeamMates, final long timeCreated, final long timeModified) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(SESSION_COLUMN_NAME, name);
        values.put(SESSION_COLUMN_SERIALIZED_TEAM_LIST, serializedTeamMates);
        values.put(SESSION_COLUMN_TIME_CREATED, timeCreated);
        values.put(SESSION_COLUMN_TIME_MODIFIED, timeModified);

        // Inserting Row
        try {
            long id = db.insert(SESSION_TABLE, null, values);

            return id;
        } finally {
            db.close();
        }
    }

    public Status updateSession(final long id, final String name, final String serializedTeamMates, final long timeCreated, final long timeModified) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(SESSION_COLUMN_NAME, name);
        values.put(SESSION_COLUMN_SERIALIZED_TEAM_LIST, serializedTeamMates);
        values.put(SESSION_COLUMN_TIME_CREATED, timeCreated);
        values.put(SESSION_COLUMN_TIME_MODIFIED, timeModified);


        // updating row
        try {
            db.update(SESSION_TABLE, values, SESSION_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public Status deleteSession(final long id) {
        final SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(SESSION_TABLE, SESSION_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});
            return Status.SUCCESS;
        } catch (SQLiteException e) {
            return Status.FAIL;
        } finally {
            db.close();
        }
    }

    public Session getSession(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(SESSION_TABLE, new String[]{
                        SESSION_COLUMN_NAME,
                        SESSION_COLUMN_SERIALIZED_TEAM_LIST,
                        SESSION_COLUMN_TIME_CREATED,
                        SESSION_COLUMN_TIME_MODIFIED
                }, SESSION_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Session session = null;

        if (cursor != null && cursor.moveToFirst()) {

            final String name = cursor.getString(cursor.getColumnIndex(SESSION_COLUMN_NAME));
            final String serializedTeamList = cursor.getString(cursor.getColumnIndex(SESSION_COLUMN_SERIALIZED_TEAM_LIST));
            final long timeCreated = cursor.getLong(cursor.getColumnIndex(SESSION_COLUMN_TIME_CREATED));
            final long timeModified = cursor.getLong(cursor.getColumnIndex(SESSION_COLUMN_TIME_MODIFIED));

            session = new Session(id, name, Session.fromJsonToTeamList(serializedTeamList), timeCreated, timeModified);
        }

        cursor.close();
        db.close();

        return session;

    }

    public List<Session> getAllSessions() {
        final List<Session> sessionList = new ArrayList<>();

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(SESSION_TABLE, new String[]{
                        SESSION_COLUMN_ID,
                        SESSION_COLUMN_NAME,
                        SESSION_COLUMN_SERIALIZED_TEAM_LIST,
                        SESSION_COLUMN_TIME_MODIFIED,
                        SESSION_COLUMN_TIME_MODIFIED
                }, null,
                null, null, null, SESSION_COLUMN_NAME + " ASC");


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {

                    final long id = cursor.getLong(cursor.getColumnIndex(SESSION_COLUMN_ID));
                    final String name = cursor.getString(cursor.getColumnIndex(SESSION_COLUMN_NAME));
                    final String serializedTeamList = cursor.getString(cursor.getColumnIndex(SESSION_COLUMN_SERIALIZED_TEAM_LIST));
                    final long timeCreated = cursor.getLong(cursor.getColumnIndex(SESSION_COLUMN_TIME_CREATED));
                    final long timeModified = cursor.getLong(cursor.getColumnIndex(SESSION_COLUMN_TIME_MODIFIED));

                    final Session session = new Session(id, name, Session.fromJsonToTeamList(serializedTeamList), timeCreated, timeModified);

                    sessionList.add(session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return group list
        return sessionList;

    }


}