package sk.libco.bestiaryfive;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class BestiaryParserJson {

    public Bestiary parse(String fileName, String stringToParse) {

        try {
            JSONArray jsonarray = new JSONArray(stringToParse);

            Bestiary bestiary = new Bestiary();
            bestiary.name = fileName;
            bestiary.id = 0;

            for (int i = 0; i < jsonarray.length(); i++) {
                try {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    if(jsonobject.has("license"))
                        continue;


                    Monster m = new Monster();
                    m.name = jsonobject.getString("name");
                    m.size = jsonobject.getString("size");
                    m.alignment = jsonobject.getString("alignment");
                    //must be after size and alignment
                    m.setType(jsonobject.getString("type") + " " + jsonobject.getString("subtype"));
                    m.ac = jsonobject.getString("armor_class");
                    m.hp = jsonobject.getString("hit_points");
                    m.speed = jsonobject.getString("speed");
                    m.str = jsonobject.getString("strength");
                    m.dex = jsonobject.getString("dexterity");
                    m.con = jsonobject.getString("constitution");
                    m.inteligence = jsonobject.getString("intelligence");
                    m.wis = jsonobject.getString("wisdom");
                    m.cha = jsonobject.getString("charisma");
                    //parse saves
                    String saves[] = {"strength_save","constitution_save","dexterity_save","intelligence_save","charisma_save"};
                    for (String save:saves) {
                        m.save = parseSaveSkill(jsonobject,m.save,save);
                    }
                    //parse skills
                    String skills[] = {"acrobatics","stealth","sleight_of_hand","arcana","history","investigation","nature","religion","insight","medicine","perception","survival","deception","intimidation","persuasion","performance"};
                    for (String skill:skills) {
                        m.skill = parseSaveSkill(jsonobject,m.skill,skill);
                    }

                    m.vulnerable = jsonobject.getString("damage_vulnerabilities");
                    m.resist = jsonobject.getString("damage_resistances");
                    m.immune = jsonobject.getString("damage_immunities");
                    m.conditionImmune = jsonobject.getString("condition_immunities");
                    m.senses = jsonobject.getString("senses");
                    m.languages = jsonobject.getString("languages");
                    m.cr = jsonobject.getString("challenge_rating");

                    try {
                        JSONArray traits = jsonobject.getJSONArray("special_abilities");

                        for (int j = 0; j < traits.length(); j++) {
                            JSONObject traitJson = traits.getJSONObject(j);
                            Monster.Trait trait = m.addTrait();

                            trait.name = traitJson.getString("name");
                            trait.text.add(traitJson.getString("desc"));

                        }
                    } catch (org.json.JSONException e) {

                    }

                    try {
                        JSONArray actions = jsonobject.getJSONArray("actions");

                        for (int j = 0; j < actions.length(); j++) {
                            JSONObject traitJson = actions.getJSONObject(j);
                            Monster.Trait trait = m.addAction();

                            trait.name = traitJson.getString("name");
                            trait.text.add(traitJson.getString("desc"));

                            /*if(traitJson.has("attack_bonus") && traitJson.has("damage_dice") && traitJson.has("damage_bonus")) {
                                trait.attack = traitJson.getString("attack_bonus") +
                                        " (" + traitJson.getString("damage_dice") + " + " +
                                        traitJson.getString("damage_bonus") + ")";
                            }*/

                        }
                    } catch (org.json.JSONException e) {

                    }

                    try {
                        JSONArray legendaryactions = jsonobject.getJSONArray("legendary_actions");

                        for (int j = 0; j < legendaryactions.length(); j++) {
                            JSONObject traitJson = legendaryactions.getJSONObject(j);
                            Monster.Trait trait = m.addLegendaryAction();

                            trait.name = traitJson.getString("name");
                            trait.text.add(traitJson.getString("desc"));

                        }
                    } catch (org.json.JSONException e) {

                    }

                    try {
                        JSONArray reactions = jsonobject.getJSONArray("reactions");

                        for (int j = 0; j < reactions.length(); j++) {
                            JSONObject traitJson = reactions.getJSONObject(j);
                            Monster.Trait trait = m.addReaction();

                            trait.name = traitJson.getString("name");
                            trait.text.add(traitJson.getString("desc"));

                        }
                    } catch (org.json.JSONException e) {

                    }

                    bestiary.monsters.AddMonster(m);

                } catch (org.json.JSONException e) {
                    Log.d("JsonParser", "Error parsing file: " + e.toString());
                }
            }

            return bestiary;

        } catch (org.json.JSONException e) {
            Log.e("JsonParser","Error parsing file: " + e.toString());
        }

        return null;

    }

    private String parseSaveSkill(JSONObject jsonobject, String existingString, String whatToParse) {

        try {
            if (jsonobject.has(whatToParse)) {
                String spacer = "";
                if(existingString == null || existingString.isEmpty()) {
                    existingString = "";
                } else {
                    spacer = ", ";
                }

                String ss = whatToParse.split("_")[0];
                ss = ss.substring(0, 1).toUpperCase() + ss.substring(1); //first letter to uppercase
                existingString += spacer + ss + " +" + jsonobject.getString(whatToParse);
            }
        } catch (org.json.JSONException e) {

        }

        return existingString;
    }

}
