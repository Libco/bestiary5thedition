package sk.libco.bestiaryfive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Monster {
    public Integer id;
    public String name;
    public String size;
    private String type;
    private String typeSimple;
    private String typeString;
    public String alignment;
    public String ac;
    public String hp;
    public String speed;
    public String str;
    public String dex;
    public String con;
    public String inteligence;
    public String wis;
    public String cha;
    public String save;
    public String skill;
    public String resist;
    public String vulnerable;
    public String immune;
    public String conditionImmune;
    //public String condition;
    public String senses;
    public String passive;
    public String languages;
    public String cr;
    public List<Trait> traits = new ArrayList<>();
    public List<Trait> actions = new ArrayList<>();
    public List<Trait> legendaryActions = new ArrayList<>();
    public List<Trait> reactions = new ArrayList<>(); //reactions - same as actions //example: bandit captain
    public String spells; //TODO: add spell list?
    public String description; // description at bottom - simmilar as actions //example:acolyte

    public boolean isDetailsLoaded = false;

    public class Trait {
        public String name;
        public List<String> text = new ArrayList<>();
        public String attack = null; //when action and not trait
    }

    public Trait addTrait() {
        Trait newTrait = new Trait();
        traits.add(newTrait);
        return newTrait;
    }

    public Trait addReaction() {
        Trait newTrait = new Trait();
        reactions.add(newTrait);
        return newTrait;
    }

    public Trait addAction() {
        Trait newAction = new Trait();
        actions.add(newAction);
        return newAction;
    }

    public Trait addLegendaryAction() {
        Trait newTrait = new Trait();
        legendaryActions.add(newTrait);
        return newTrait;
    }

    public String getSizeString() {
        String sizeString = "Unknown size";
        if(size != null && !size.isEmpty()) {
            switch (size.toLowerCase()) {
                case "t":
                    sizeString = "Tiny";
                    break;
                case "s":
                    sizeString = "Small";
                    break;
                case "m":
                    sizeString = "Medium";
                    break;
                case "l":
                    sizeString = "Large";
                    break;
                case "h":
                    sizeString = "Huge";
                    break;
                case "g":
                    sizeString = "Gargantuan";
                    break;
                default:
                    sizeString = size;
            }
        }
        return sizeString;
    }

    public String getType() {
        return type;
    }

    public String getTypeString() {
        return typeString;
    }

    public String getTypeSimple() {
      return typeSimple;
    }

    public void setType(String t) {
        if(t == null) {
            t = "unknown";
        }

        type = t;

        typeString = getSizeString() + " " + t;
        if(alignment != null && !alignment.isEmpty()) {
            typeString += ", " + alignment;
        }

        typeSimple = t;
        typeSimple = typeSimple.split(" ")[0];
        typeSimple = typeSimple.toLowerCase();
        typeSimple = Character.toString(typeSimple.charAt(0)).toUpperCase()+typeSimple.substring(1);
        typeSimple = typeSimple.replaceAll("[^A-Za-z0-9]", "");
    }

    public final static List<String> CR_ORDER = Arrays.asList("0", "1/8", "1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
    public final static List<String> SIZE_ORDER = Arrays.asList("Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan");


    public static String getAbilityWithBonus(String ability) {
        return String.format("%s (%s)", ability, getBonus(ability));
    }

    public List<Trait> getFeatures() {

        List<Trait> featureList = new ArrayList<>();

        checkFeature(featureList,"Saving throws",save);
        checkFeature(featureList,"Skills",skill);
        checkFeature(featureList,"Damage Resistances",resist);
        checkFeature(featureList,"Damage Vulnerabilities",vulnerable);
        checkFeature(featureList,"Damage Immunities",immune);
        checkFeature(featureList,"Condition Immunities",conditionImmune);
        if(senses != null && !senses.isEmpty()) {
            if(passive != null && !passive.isEmpty())
                checkFeature(featureList,"Senses", senses + ", passive Perception " + passive);
            else
                checkFeature(featureList,"Senses", senses);
        } else if (passive != null && !passive.isEmpty())
            checkFeature(featureList,"Senses", "passive Perception " + passive);
        checkFeature(featureList,"Languages",languages);
        checkFeature(featureList,"Challenge",cr);

        return featureList;
    }

    private void checkFeature(List<Trait> featureList, String featureLabel, String feature) {
        if(feature != null && !feature.isEmpty()) {
            Trait trait = new Trait();
            trait.name = featureLabel;
            trait.text.add(feature);
            featureList.add(trait);
        }
    }

    private static String getBonus(String stat) {
        try {
            int statInt = Integer.parseInt(stat);
            int statBonus = (statInt - 10) / 2;
            if (statBonus < -5) {
                statBonus = -5;
            }
            //
            if (statBonus >= 0) {
                return "+" + statBonus;
            } else {
                return "" + statBonus;
            }
        } catch (Exception e) {
            //propably wrong format...
        }

        return "";
    }

}
