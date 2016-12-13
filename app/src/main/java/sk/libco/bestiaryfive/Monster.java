package sk.libco.bestiaryfive;

import java.util.ArrayList;
import java.util.List;

public class Monster {
    public Integer id;
    public String name;
    public String size;
    public String type;
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

    public String getTypeString() {
        String typeString = "";
        if(size != null && !size.isEmpty()) {

            switch (size.toLowerCase()) {
                case "t":
                    typeString = "Tiny";
                    break;
                case "s":
                    typeString = "Small";
                    break;
                case "m":
                    typeString = "Medium";
                    break;
                case "l":
                    typeString = "Large";
                    break;
                case "h":
                    typeString = "Huge";
                    break;
                case "g":
                    typeString = "Gargantuan";
                    break;
                default:
                    typeString = size;
            }
            typeString += " ";
        }
        typeString += type;

        if(alignment != null && !alignment.isEmpty())
            typeString += ", " + alignment;

        return typeString;
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
}
