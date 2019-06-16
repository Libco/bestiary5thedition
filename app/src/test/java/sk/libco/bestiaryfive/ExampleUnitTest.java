package sk.libco.bestiaryfive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {

    @Test
    public void testXmlImport() throws  Exception {
//
//        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<!-- Updated 2015/12/21 (Errata) -->\n" +
//                "<compendium version=\"5\">\n" +
//                "<monster>\n" +
//                "\t\t<name>TestMonst</name>\n" +
//                "\t\t<size>L</size>\n" +
//                "\t\t<type>aberration, monster manual</type>\n" +
//                "\t\t<alignment>lawful evil</alignment>\n" +
//                "\t\t<ac>18 (natural armor)</ac>\n" +
//                "\t\t<hp>42 (19d10+76)</hp>\n" +
//                "\t\t<speed>0 ft., fly 20 ft. (hover)</speed>\n" +
//                "\t\t<str>10</str>\n" +
//                "\t\t<dex>14</dex>\n" +
//                "\t\t<con>18</con>\n" +
//                "\t\t<int>17</int>\n" +
//                "\t\t<wis>15</wis>\n" +
//                "\t\t<cha>17</cha>\n" +
//                "\t\t<save>Int +8, Wis +7, Cha +8</save>\n" +
//                "\t\t<skill>Perception +12</skill>\n" +
//                "\t\t<conditionImmune>prone</conditionImmune>\n" +
//                "\t\t<senses>darkvision 120 ft.</senses>\n" +
//                "\t\t<passive>22</passive>\n" +
//                "\t\t<languages>Deep Speech, Undercommon</languages>\n" +
//                "\t\t<cr>13</cr>\n" +
//                "\t\t<trait>\n" +
//                "\t\t\t<name>Antimagic Cone</name>\n" +
//                "\t\t\t<text>The testmonst's central eye creates an area of antimagic, as in the antimagic field spell, in a 150-foot cone. At the start of each of its turns, the testmonst decides which way the cone faces and whether the cone is active. The area works against the testmonst's own eye rays.</text>\n" +
//                "\t\t</trait>\n" +
//                "\t\t<action>\n" +
//                "\t\t\t<name>Bite</name>\n" +
//                "\t\t\t<text>Melee Weapon Attack: +5 to hit, reach 5 ft., one target. Hit: 14 (4d6) piercing damage.</text>\n" +
//                "\t\t\t<attack>Bite|5|4d6</attack>\n" +
//                "\t\t</action>\n" +
//                "\t\t<action>\n" +
//                "\t\t\t<name>Eye Rays</name>\n" +
//                "\t\t\t<text>The testmonst shoots three of the following magical eye rays at random (reroll duplicates), choosing one to three targets it can see within 120 ft. of it:</text>\n" +
//                "\t\t\t<text>1. Charm Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or be charmed by the testmonst for 1 hour, or until the testmonst harms the creature.</text>\n" +
//                "\t\t\t<text>2. Paralyzing Ray. The targeted creature must succeed on a DC 16 Constitution saving throw or be paralyzed for 1 minute. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.</text>\n" +
//                "\t\t\t<text>3. Fear Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or be frightened for 1 minute. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.</text>\n" +
//                "\t\t\t<text>4. Slowing Ray. The targeted creature must succeed on a DC 16 Dexterity saving throw. On a failed save, the target's speed is halved for 1 minute. In addition, the creature can't take reactions, and it can take either an action or a bonus action on its turn, not both. The creature can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.</text>\n" +
//                "\t\t\t<text>5. Enervation Ray. The targeted creature must make a DC 16 Constitution saving throw, taking 36 (8d8) necrotic damage on a failed save, or half as much damage on a successful one.</text>\n" +
//                "\t\t\t<text>6. Telekinetic Ray. If the target is a creature, it must succeed on a DC 16 Strength saving throw or the testmonst moves it up to 30 ft. in any direction. It is restrained by the ray's telekinetic grip until the start of the testmonst's next turn or until the testmonst is incapacitated.</text>\n" +
//                "\t\t\t<text>If the target is an object weighing 300 pounds or less that isn't being worn or carried, it is moved up to 30 ft. in any direction. The testmonst can also exert fine control on objects with this ray, such as manipulating a simple tool or opening a door or a container.</text>\n" +
//                "\t\t\t<text>7. Sleep Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or fall asleep and remain unconscious for 1 minute. The target awakens if it takes damage or another creature takes an action to wake it. This ray has no effect on constructs and undead.</text>\n" +
//                "\t\t\t<text>8. Petrification Ray. The targeted creature must make a DC 16 Dexterity saving throw. On a failed save, the creature begins to turn to stone and is restrained. It must repeat the saving throw at the end of its next turn. On a success, the effect ends. On a failure, the creature is petrified until freed by the greater restoration spell or other magic.</text>\n" +
//                "\t\t\t<text>9. Disintegration Ray. If the target is a creature, it must succeed on a DC 16 Dexterity saving throw or take 45 (10d8) force damage. If this damage reduces the creature to 0 hit points, its body becomes a pile of fine gray dust.</text>\n" +
//                "\t\t\t<text>If the target is a Large or smaller nonmagical object or creation of magical force, it is disintegrated without a saving throw. If the target is a Huge or larger object or creation of magical force, this ray disintegrates a 10-foot cube of it.</text>\n" +
//                "\t\t\t<text>10. Death Ray. The targeted creature must succeed on a DC 16 Dexterity saving throw or take 55 (10d10) necrotic damage. The target dies if the ray reduces it to 0 hit points.</text>\n" +
//                "\t\t</action>\n" +
//                "\t\t<legendary>\n" +
//                "\t\t\t<name>Eye Ray</name>\n" +
//                "\t\t\t<text>The testmonst uses one random eye ray.</text>\n" +
//                "\t\t</legendary>\n" +
//                "\t</monster>\n" +
//                "</compendium>";
//
//        InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
//
//
//        BestiaryParser bestiaryParser = new BestiaryParser();
//        Bestiary bestiary = new Bestiary();
//        bestiary.monsters = bestiaryParser.parse(xmlStream);
//        assertEquals(bestiary.monsters.size(), 1);
//
//        Monster testMonst = bestiary.monsters.get(0);
//        assertEquals(testMonst.name, "TestMonst");
//        assertEquals(testMonst.ac, "18 (natural armor)");
//        assertEquals(testMonst.actions.size(), 2);
//        assertEquals(testMonst.actions.get(1).text.size(), 13);


    }
}