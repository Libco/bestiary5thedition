package sk.libco.bestiaryfive;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Bestiary {

    public Integer id;
    public String name;
    public Uri uri;
    public List<Monster> monsters;

    public Bestiary() {
        monsters = new ArrayList<>();
    }

}
