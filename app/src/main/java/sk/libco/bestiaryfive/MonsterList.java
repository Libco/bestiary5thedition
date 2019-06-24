package sk.libco.bestiaryfive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MonsterList {

    private List<Monster> monsters = new ArrayList<>();
    private ArrayList<String> typeFilter = new ArrayList<>();
    private ArrayList<String> sizeFilter = new ArrayList<>();
    private ArrayList<String> alignementFilter = new ArrayList<>();

    //
    public static final Comparator<String> SIZE_COMPARATOR = (String o1, String o2) -> Monster.SIZE_ORDER.indexOf(o1) - Monster.SIZE_ORDER.indexOf(o2);
    //

    public void AddMonster(Monster m) {
        monsters.add(m);

        if(!typeFilter.contains(m.getTypeSimple())) {
            typeFilter.add(m.getTypeSimple());
            Collections.sort(typeFilter);
        }

        if(!sizeFilter.contains(m.getSizeString())) {
            sizeFilter.add(m.getSizeString());
            Collections.sort(sizeFilter, SIZE_COMPARATOR);
        }
        if(!alignementFilter.contains(m.alignment)) {
            alignementFilter.add(m.alignment);
            Collections.sort(alignementFilter);
        }

    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    ///
    public ArrayList<String> getTypeFilter() {
        return typeFilter;
    }

    public ArrayList<String> getSizeFilter() {
        return sizeFilter;
    }

    public ArrayList<String> getAlignementFilter() {
        return alignementFilter;
    }
}