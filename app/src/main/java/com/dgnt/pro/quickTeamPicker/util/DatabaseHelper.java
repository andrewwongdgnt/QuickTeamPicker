package com.dgnt.pro.quickTeamPicker.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dgnt.pro.quickTeamPicker.holder.Group;
import com.dgnt.pro.quickTeamPicker.holder.Person;
import com.dgnt.pro.quickTeamPicker.holder.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 10/11/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";

    private static final String PERSON_TABLE = "personTable";
    private static final String PERSON_COLUMN_ID = "id";
    private static final String PERSON_COLUMN_NAME = "name";
    private static final String PERSON_COLUMN_SKILL = "skill";
    private static final String PERSON_COLUMN_GROUP_ID = "groupId";

    private static final String GROUP_TABLE = "groupTable";
    private static final String GROUP_COLUMN_ID = "id";
    private static final String GROUP_COLUMN_NAME = "name";

    private static final String SESSION_TABLE = "sessionTable";
    private static final String SESSION_COLUMN_ID = "id";
    private static final String SESSION_COLUMN_NAME = "name";
    private static final String SESSION_COLUMN_SERIALIZED_TEAM_LIST = "teamList";
    private static final String SESSION_COLUMN_TIME_CREATED = "timeCreated";
    private static final String SESSION_COLUMN_TIME_MODIFIED = "timeModified";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //Create Person Table
        final String createPersonTableQuery = "CREATE TABLE " + PERSON_TABLE + "(" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_COLUMN_NAME + " TEXT, " +
                PERSON_COLUMN_SKILL + " INTEGER, " +
                PERSON_COLUMN_GROUP_ID + " TEXT " +
                ")";

        db.execSQL(createPersonTableQuery);

        //Create Group Table
        final String createGroupTableQuery = "CREATE TABLE " + GROUP_TABLE + "(" +
                GROUP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GROUP_COLUMN_NAME + " TEXT " +
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
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);

        onCreate(db);
    }

    //------------------
    // Person
    //------------------

    public long addPerson(final String name, final int skill, final int groupId) {

        final SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;

        //Inserting Person
        values = new ContentValues();
        values.put(PERSON_COLUMN_NAME, name);
        values.put(PERSON_COLUMN_SKILL, skill);
        values.put(PERSON_COLUMN_GROUP_ID, groupId);

        final long personId = db.insert(PERSON_TABLE, null, values);

        db.close(); // Closing database connection

        return personId;
    }

    public void updatePerson(final long id, final String name, final int skill, final int groupId) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(PERSON_COLUMN_NAME, name);
        values.put(PERSON_COLUMN_SKILL, skill);
        values.put(PERSON_COLUMN_GROUP_ID, groupId);


        // updating row
        db.update(PERSON_TABLE, values, PERSON_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePerson(final long id) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PERSON_TABLE, PERSON_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Person getPerson(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(PERSON_TABLE, new String[]{
                        PERSON_COLUMN_NAME,
                        PERSON_COLUMN_SKILL,
                        PERSON_COLUMN_GROUP_ID
                }, PERSON_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Person person = null;

        if (cursor != null && cursor.moveToFirst()) {

            final String name = cursor.getString(cursor.getColumnIndex(PERSON_COLUMN_NAME));
            final int skill = cursor.getInt(cursor.getColumnIndex(PERSON_COLUMN_SKILL));
            final long groupId = cursor.getLong(cursor.getColumnIndex(PERSON_COLUMN_GROUP_ID));


            person = new Person(id, name, skill, groupId);
        }

        cursor.close();
        db.close();

        return person;

    }


    //------------------
    // Groups
    //------------------
    public long addGroup(final String name) {

        final SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;

        //Insert into group
        values = new ContentValues();
        values.put(GROUP_COLUMN_NAME, name);

        long id = db.insert(GROUP_TABLE, null, values);

        db.close(); // Closing database connection

        return id;
    }

    public void updateGroup(final long id, final String name) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(GROUP_COLUMN_NAME, name);

        // updating row
        db.update(GROUP_TABLE, values, GROUP_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteGroup(final long id) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GROUP_TABLE, GROUP_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Group> getAllGroups() {
        final List<Group> groupList = new ArrayList<>();

        final SQLiteDatabase db = this.getReadableDatabase();

        final Map<Long, List<Person>> groupMap = new HashMap<>();

        final Cursor cursor = db.query(PERSON_TABLE, new String[]{
                        PERSON_COLUMN_ID,
                        PERSON_COLUMN_NAME,
                        PERSON_COLUMN_SKILL,
                        PERSON_COLUMN_GROUP_ID
                }, null,
                null, null, null, PERSON_COLUMN_NAME + " ASC");


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {
                    final long id = cursor.getLong(cursor.getColumnIndex(PERSON_COLUMN_ID));
                    final String name = cursor.getString(cursor.getColumnIndex(PERSON_COLUMN_NAME));
                    final int skill = cursor.getInt(cursor.getColumnIndex(PERSON_COLUMN_SKILL));
                    final long groupId = cursor.getLong(cursor.getColumnIndex(PERSON_COLUMN_GROUP_ID));


                    final Person person = new Person(id, name, skill, groupId);

                    if (!groupMap.containsKey(groupId))
                        groupMap.put(groupId, new ArrayList<Person>());

                    final List<Person> personList = groupMap.get(groupId);
                    personList.add(person);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        final Cursor cursor2 = db.query(GROUP_TABLE, new String[]{
                        GROUP_COLUMN_ID,
                        GROUP_COLUMN_NAME
                }, null,
                null, null, null, GROUP_COLUMN_NAME + " ASC");

        if (cursor2.moveToFirst()) {
            do {
                try {
                    final long id = cursor2.getLong(cursor2.getColumnIndex(GROUP_COLUMN_ID));
                    final String name = cursor2.getString(cursor2.getColumnIndex(GROUP_COLUMN_NAME));

                    final Group group = new Group(id, name, groupMap.get(id));

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
        long id = db.insert(SESSION_TABLE, null, values);
        db.close(); // Closing database connection

        return id;
    }

    public void updateSession(final long id, final String name, final String serializedTeamMates, final long timeCreated, final long timeModified) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(SESSION_COLUMN_NAME, name);
        values.put(SESSION_COLUMN_SERIALIZED_TEAM_LIST, serializedTeamMates);
        values.put(SESSION_COLUMN_TIME_CREATED, timeCreated);
        values.put(SESSION_COLUMN_TIME_MODIFIED, timeModified);


        // updating row
        db.update(SESSION_TABLE, values, SESSION_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteSession(final long id) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_TABLE, SESSION_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
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