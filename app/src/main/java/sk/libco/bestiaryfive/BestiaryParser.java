package sk.libco.bestiaryfive;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BestiaryParser {

    private static final String TAG = "BestiaryParser";

    private static final String ns = null;

    public List<Monster> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Monster> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Monster> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "compendium");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Log.d(TAG, "trying to parse: " + name);
            // Starts by looking for the entry tag
            if (name.equals("monster")) {
                //Log.d(TAG, "parsing: " + name);
                entries.add(readEntry(parser));
            } else {
                //Log.d(TAG, "skipping");
                skip(parser);
            }
        }
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private Monster readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "monster");

        Monster monster = new Monster();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();

            try {
                //Check special fields first
                if(parserName.equals("int"))
                    monster.inteligence = readString(parser,parserName);
                else if(parserName.equals("trait")) {
                    Monster.Trait newTrait = monster.addTrait();
                    readTraits(parser, newTrait, parserName);
                    //Log.d(TAG,"added new trait: " + newTrait.name);
                }
                else if(parserName.equals("action")) {
                    Monster.Trait newAction = monster.addAction();
                    readActions(parser, newAction);
                    //Log.d(TAG,"added new action: " + newAction.name);
                }
                else if(parserName.equals("legendary")) {
                    Monster.Trait newlegendary = monster.addLegendaryAction();
                    readTraits(parser, newlegendary, parserName);
                    //Log.d(TAG,"added new legendary: " + newlegendary.name);
                }
                else if(parserName.equals("reaction")) {
                    Monster.Trait newTrait = monster.addReaction();
                    readTraits(parser, newTrait, parserName);
                    //Log.d(TAG,"added new reaction: " + newTrait.name);
                }
                else{
                    Field field = Monster.class.getField(parserName);
                    field.set(monster, readString(parser, parserName));
                    //Log.d(TAG, "Parsed " + field.getName());
                }

            } catch (Exception e) {
                Log.d(TAG, "Does not exist: " + parserName + ". error: " + e.toString());
                skip(parser);
            }

        }
        return monster;
    }

    private Monster.Trait readTraits(XmlPullParser parser, Monster.Trait trait, String tagToExpect) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, tagToExpect);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("name")) {
                trait.name = readString(parser,name);
            } else if(name.equals("text")) {
                trait.text.add(readString(parser,name));
            } else {
                //Log.d(TAG, "skipping");
                skip(parser);
            }
        }

        return trait;
    }

    private Monster.Trait readActions(XmlPullParser parser, Monster.Trait action) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "action");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("name")) {
                action.name = readString(parser,name);
            } else if(name.equals("text")) {
                action.text.add(readString(parser,name));
            } else if(name.equals("attack")) {
                action.attack = readString(parser,name);
            }
            else {
                //Log.d(TAG, "skipping");
                skip(parser);
            }
        }

        return action;
    }

    private String readString(XmlPullParser parser, String str) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, str);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, str);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
