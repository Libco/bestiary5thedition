package sk.libco.bestiaryfive.ui.models;

import java.util.Comparator;

import sk.libco.bestiaryfive.Monster;

public class MonsterViewModel {

    public final Monster m;

    public MonsterViewModel(Monster m) {
        this.m = m;
    }


    public static class Comparators {
        public static final Comparator<MonsterViewModel> NAME = (MonsterViewModel o1, MonsterViewModel o2) -> o1.m.name.compareTo(o2.m.name);
        public static final Comparator<MonsterViewModel> CR = (MonsterViewModel o1, MonsterViewModel o2) -> Monster.CR_ORDER.indexOf(o1.m.cr) - Monster.CR_ORDER.indexOf(o2.m.cr);
        //public static final Comparator<MonsterViewModel> CRANDNAME = (MonsterViewModel o1, MonsterViewModel o2) -> CR.thenComparing(NAME).compare(o1, o2);
    }

/*
    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof MonsterViewModel) {
            final MonsterViewModel wordModel = (MonsterViewModel) item;
            return wordModel.m.id.equals(m.id);
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof MonsterViewModel) {
            final MonsterViewModel other = (MonsterViewModel) item;
            if (!m.id.equals(other.m.id)) {
                return false;
            }
            return m.name != null ? m.name.equals(other.m.name) : other.m.name == null;
        }
        return false;
    }
    */
}
