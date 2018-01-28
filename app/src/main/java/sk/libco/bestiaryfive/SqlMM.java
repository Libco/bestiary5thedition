package sk.libco.bestiaryfive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SqlMM extends SQLiteOpenHelper {

    private static final String TAG = "SQLMM";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mm.db";
    private static final String TABLE_MM = "bestiary";
    private static final String TABLE_MM_ID = "id";
    private static final String TABLE_MM_NAME = "name";
    private static final String TABLE_M = "monster";
    private static final String TABLE_M_ID = "id";
    private static final String TABLE_M_BESTIARY_ID = "bestiary_id";
    private static final String TABLE_M_NAME = "name";
    private static final String TABLE_M_SIZE = "size";
    private static final String TABLE_M_TYPE = "type";
    private static final String TABLE_M_ALIGNMENT = "alignment";
    private static final String TABLE_M_AC = "ac";
    private static final String TABLE_M_HP = "hp";
    private static final String TABLE_M_SPEED = "speed";
    private static final String TABLE_M_STR = "str";
    private static final String TABLE_M_DEX = "dex";
    private static final String TABLE_M_CON = "con";
    private static final String TABLE_M_INTELIGENCE = "inteligence";
    private static final String TABLE_M_WIS = "wis";
    private static final String TABLE_M_CHA = "cha";
    private static final String TABLE_M_SAVE = "save";
    private static final String TABLE_M_SKILL = "skill";
    private static final String TABLE_M_RESIST = "resist";
    private static final String TABLE_M_VULNERABLE = "vulnerable";
    private static final String TABLE_M_IMMUNE = "immune";
    private static final String TABLE_M_CONDITION_IMMUNE = "condition_immune";
    //private static final String TABLE_M_CONDITION = "condition";
    private static final String TABLE_M_SENSES = "senses";
    private static final String TABLE_M_PASSIVE = "passive";
    private static final String TABLE_M_LANGUAGES = "languages";
    private static final String TABLE_M_CR = "cr";
    //private static final String TABLE_M_TRAITS = "traits";
    //private static final String TABLE_M_ACTIONS = "actions";
    //private static final String TABLE_M_LEGENDARY_ACTIONS = "legendary_actions";
    //private static final String TABLE_M_REACTIONS = "reactions";
    private static final String TABLE_M_SPELLS = "spells";
    private static final String TABLE_M_DESCRIPTION = "description";
    private static final String TABLE_M_TRAIT = "trait";
    private static final String TABLE_M_TRAIT_ID = "id";
    private static final String TABLE_M_TRAIT_TYPE = "type";
    private static final String TABLE_M_TRAIT_MONSTER_ID = "monster_id";
    private static final String TABLE_M_TRAIT_NAME = "name";
    private static final String TABLE_M_TRAIT_TEXT = "text";
    private static final String TABLE_M_TRAIT_ATTACK = "attack";
    private static SqlMM mInstance = null;


    private SqlMM(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static SqlMM getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new SqlMM(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"Creating db");

        String CREATE_MM_TABLE = "CREATE TABLE " + TABLE_MM +
                "(" +
                TABLE_MM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_MM_NAME + " STRING" +
                ")";

        String CREATE_M_TABLE = "CREATE TABLE " + TABLE_M +
                "(" +
                TABLE_M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_M_BESTIARY_ID + " INTEGER REFERENCES " + TABLE_MM + " ON DELETE CASCADE," +
                TABLE_M_NAME + " STRING" +  "," +
                TABLE_M_SIZE + " STRING" + "," +
                TABLE_M_TYPE + " STRING" + "," +
                TABLE_M_ALIGNMENT + " STRING" + "," +
                TABLE_M_AC + " INTEGER" + "," +
                TABLE_M_HP + " INTEGER" + "," +
                TABLE_M_SPEED + " INTEGER" + "," +
                TABLE_M_STR + " INTEGER" + "," +
                TABLE_M_DEX + " INTEGER" + "," +
                TABLE_M_CON + " INTEGER" + "," +
                TABLE_M_INTELIGENCE + " INTEGER" + "," +
                TABLE_M_WIS + " INTEGER" + "," +
                TABLE_M_CHA + " INTEGER" + "," +
                TABLE_M_SAVE + " STRING" + "," +
                TABLE_M_SKILL + " STRING" + "," +
                TABLE_M_RESIST + " STRING" + "," +
                TABLE_M_VULNERABLE + " STRING" + "," +
                TABLE_M_IMMUNE + " STRING" + "," +
                TABLE_M_CONDITION_IMMUNE + " STRING" + "," +
               // TABLE_M_CONDITION + " STRING," +
                TABLE_M_SENSES + " STRING" + "," +
                TABLE_M_PASSIVE + " STRING" + "," +
                TABLE_M_LANGUAGES + " STRING" + "," +
                TABLE_M_CR + " STRING" + "," +
                //TABLE_M_TRAITS + " STRING," +
                //TABLE_M_ACTIONS + " STRING," +
                //TABLE_M_LEGENDARY_ACTIONS + " STRING," +
                //TABLE_M_REACTIONS + " STRING," +
                TABLE_M_SPELLS + " STRING" + "," +
                TABLE_M_DESCRIPTION + " STRING" +
                ")";

        String CREATE_TRAIT_TABLE = "CREATE TABLE " + TABLE_M_TRAIT +
                "(" +
                TABLE_M_TRAIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_M_TRAIT_MONSTER_ID + " INTEGER REFERENCES " + TABLE_M + " ON DELETE CASCADE," +
                TABLE_M_TRAIT_TYPE + " INTEGER" + "," +
                TABLE_M_TRAIT_NAME + " STRING" + "," +
                TABLE_M_TRAIT_TEXT + " STRING" + "," +
                TABLE_M_TRAIT_ATTACK + " STRING" +
                ")";


        db.execSQL(CREATE_M_TABLE);
        db.execSQL(CREATE_MM_TABLE);
        db.execSQL(CREATE_TRAIT_TABLE);

        Log.d(TAG,"DB created");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Nothing for now
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*********************************************************************/

    private void insertTrait(SQLiteDatabase db, List<Monster.Trait> traits, long monsterID, int type) throws Exception {

        for (Monster.Trait trait : traits) {
            ContentValues trait_values = new ContentValues();

            trait_values.put(TABLE_M_TRAIT_MONSTER_ID, monsterID);
            trait_values.put(TABLE_M_TRAIT_TYPE, type);
            trait_values.put(TABLE_M_TRAIT_NAME, trait.name);
            StringBuilder sb = new StringBuilder();
            for (String t:trait.text) {
                sb.append(t).append("\n");
            }
            String text = sb.toString();
            trait_values.put(TABLE_M_TRAIT_TEXT, text);
            trait_values.put(TABLE_M_TRAIT_ATTACK, trait.attack);

            db.insertOrThrow(TABLE_M_TRAIT, null, trait_values);
        }

    }

    // Insert a post into the database
    void addBestiary(Bestiary bestiary) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_MM_NAME, bestiary.name);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            long id = db.insertOrThrow(TABLE_MM, null, values);

            for (Monster m:bestiary.monsters) {

                ContentValues monster_values = new ContentValues();

                monster_values.put(TABLE_M_BESTIARY_ID, id);
                monster_values.put(TABLE_M_NAME, m.name);
                monster_values.put(TABLE_M_SIZE, m.size);
                monster_values.put(TABLE_M_TYPE, m.type);
                monster_values.put(TABLE_M_ALIGNMENT, m.alignment);
                monster_values.put(TABLE_M_AC, m.ac);
                monster_values.put(TABLE_M_HP, m.hp);
                monster_values.put(TABLE_M_SPEED, m.speed);
                monster_values.put(TABLE_M_STR, m.str);
                monster_values.put(TABLE_M_DEX, m.dex);
                monster_values.put(TABLE_M_CON, m.con);
                monster_values.put(TABLE_M_INTELIGENCE, m.inteligence);
                monster_values.put(TABLE_M_WIS, m.wis);
                monster_values.put(TABLE_M_CHA, m.cha);
                monster_values.put(TABLE_M_SAVE, m.save);
                monster_values.put(TABLE_M_SKILL, m.skill);
                monster_values.put(TABLE_M_RESIST, m.resist);
                monster_values.put(TABLE_M_VULNERABLE, m.vulnerable);
                monster_values.put(TABLE_M_IMMUNE, m.immune);
                monster_values.put(TABLE_M_CONDITION_IMMUNE, m.conditionImmune);
                monster_values.put(TABLE_M_SENSES, m.senses);
                monster_values.put(TABLE_M_PASSIVE, m.passive);
                monster_values.put(TABLE_M_LANGUAGES, m.languages);
                monster_values.put(TABLE_M_CR, m.cr);
                monster_values.put(TABLE_M_SPELLS, m.spells);
                monster_values.put(TABLE_M_DESCRIPTION, m.description);

                long idM = db.insertOrThrow(TABLE_M, null, monster_values);

                insertTrait(db, m.traits, idM, 0); //type 0 is traits
                insertTrait(db, m.actions, idM, 1); //type 1 is actions
                insertTrait(db, m.legendaryActions, idM, 2); //type 2 is legendary actions
                insertTrait(db, m.reactions, idM, 3); //type 3 is reactions

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add bestiary to database");
        } finally {
            db.endTransaction();
        }


        Log.d(TAG,"Added bestiary to db");

    }

    void deleteBestiary(int idToDelete) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MM, TABLE_MM_ID+"=?", new String[] { String.valueOf(idToDelete) });

    }

    List<Bestiary> getAllBestiaries() {
        List<Bestiary> bestiaries = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT " + TABLE_MM_ID + ", " + TABLE_MM_NAME + " FROM " + TABLE_MM;
                           // " LEFT JOIN " + TABLE_M + "m ON mm." + TABLE_MM_ID + " = m." + TABLE_M_BESTIARY_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorMM = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursorMM.moveToFirst()) {
            do {
                Bestiary newBestiary = new Bestiary();
                newBestiary.id = cursorMM.getInt(0);
                newBestiary.name = cursorMM.getString(1);


                String selectMonstersQuery = "SELECT " +
                        TABLE_M_ID + "," +
                        TABLE_M_NAME + "," +
                        TABLE_M_SIZE + "," +
                        TABLE_M_TYPE + "," +
                        TABLE_M_ALIGNMENT + "," +
                        TABLE_M_AC + "," +
                        TABLE_M_HP + "," +
                        TABLE_M_SPEED + "," +
                        TABLE_M_STR + "," +
                        TABLE_M_DEX + "," +
                        TABLE_M_CON + "," +
                        TABLE_M_INTELIGENCE + "," +
                        TABLE_M_WIS + "," +
                        TABLE_M_CHA + "," +
                        TABLE_M_SAVE + "," +
                        TABLE_M_SKILL + "," +
                        TABLE_M_RESIST + "," +
                        TABLE_M_VULNERABLE + "," +
                        TABLE_M_IMMUNE + "," +
                        TABLE_M_CONDITION_IMMUNE + "," +
                        TABLE_M_SENSES + "," +
                        TABLE_M_PASSIVE + "," +
                        TABLE_M_LANGUAGES + "," +
                        TABLE_M_CR + "," +
                        TABLE_M_SPELLS + "," +
                        TABLE_M_DESCRIPTION +
                        " FROM " + TABLE_M + " WHERE " + TABLE_M_BESTIARY_ID + " = " + newBestiary.id;

                Cursor cursorM = db.rawQuery(selectMonstersQuery, null);

                if (cursorM.moveToFirst()) {
                    do {
                        Monster m = new Monster();
                        m.id = cursorM.getInt(0);
                        m.name = cursorM.getString(1);
                        m.size = cursorM.getString(2);
                        m.type = cursorM.getString(3);
                        m.alignment = cursorM.getString(4);
                        m.ac = cursorM.getString(5);
                        m.hp = cursorM.getString(6);
                        m.speed = cursorM.getString(7);
                        m.str = cursorM.getString(8);
                        m.dex = cursorM.getString(9);
                        m.con = cursorM.getString(10);
                        m.inteligence = cursorM.getString(11);
                        m.wis = cursorM.getString(12);
                        m.cha = cursorM.getString(13);
                        m.save = cursorM.getString(14);
                        m.skill = cursorM.getString(15);
                        m.resist = cursorM.getString(16);
                        m.vulnerable = cursorM.getString(17);
                        m.immune = cursorM.getString(18);
                        m.conditionImmune = cursorM.getString(19);
                        m.senses = cursorM.getString(20);
                        m.passive = cursorM.getString(21);
                        m.languages = cursorM.getString(22);
                        m.cr = cursorM.getString(23);
                        m.spells = cursorM.getString(24);
                        m.description = cursorM.getString(25);
                        newBestiary.monsters.add(m);
                    } while (cursorM.moveToNext());
                }

                cursorM.close();

                bestiaries.add(newBestiary);
            } while (cursorMM.moveToNext());
        }

        cursorMM.close();

        // return contact list
        return bestiaries;
    }

    void loadMonsterDetails(Monster monster) {

        if(monster.isDetailsLoaded)
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        String selectMonsterDetailsQuery = "SELECT " +
                TABLE_M_TRAIT_TYPE + "," +
                TABLE_M_TRAIT_NAME + "," +
                TABLE_M_TRAIT_TEXT + "," +
                TABLE_M_TRAIT_ATTACK +
                " FROM " + TABLE_M_TRAIT + " WHERE " + TABLE_M_TRAIT_MONSTER_ID + " = " + monster.id;

        Cursor cursorMT = db.rawQuery(selectMonsterDetailsQuery, null);

        if (cursorMT.moveToFirst()) {
            do {

                int traitType = cursorMT.getInt(0);
                Monster.Trait trait = null;

                switch (traitType) {
                    case 0:
                        trait = monster.addTrait();
                        break;
                    case 1:
                        trait = monster.addAction();
                        break;
                    case 2:
                        trait = monster.addLegendaryAction();
                        break;
                    case 3:
                        trait = monster.addReaction();
                        break;
                    default:
                        break;
                }

                if(trait != null) {
                    trait.name = cursorMT.getString(1);
                    String texts[] = cursorMT.getString(2).split("\n");
                    trait.text.addAll(Arrays.asList(texts));
                    trait.attack = cursorMT.getString(3);
                }

            } while (cursorMT.moveToNext());
        }

        cursorMT.close();

        monster.isDetailsLoaded = true;

    }


}
