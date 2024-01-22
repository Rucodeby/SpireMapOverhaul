package spireMapOverhaul.zones.lazer;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class LazerEvent extends PhasedEvent {

    public static final String ID = makeID("LazerEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    private final int baseChance = 20;
    private int tmpChance = AbstractDungeon.miscRng.random(10, 20);

    // use AbstractDungeon.miscRng.random(0, 99);

    public LazerEvent() {
        super(ID, title, "images/events/theNest.jpg");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey("base")));

        registerPhase("base", new TextPhase(DESCRIPTIONS[1])
                // Add conditions for next 3 options
                .addOption(OPTIONS[1], (i) -> transitionKey("GoldGrid"))
                .addOption(OPTIONS[2], (i) -> transitionKey("HpGrid"))
                .addOption(String.format(OPTIONS[3], baseChance + tmpChance), (i) -> tryObtain())
                .addOption(OPTIONS[4], (i) -> {
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    transitionKey("Leave");
                })
        ); // i'd rather to not copypaste options for every phase, but i can't figure out how

        registerPhase("FailedDrone", new TextPhase(DESCRIPTIONS[4] + DESCRIPTIONS[6])
                .addOption(OPTIONS[1], (i) -> transitionKey("GoldGrid"))
                .addOption(OPTIONS[2], (i) -> transitionKey("HpGrid"))
                .addOption(String.format(OPTIONS[3], baseChance + tmpChance), (i) -> tryObtain())
                .addOption(OPTIONS[4], (i) -> {
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    transitionKey("Leave");
                })
        );

        registerPhase("AfterGrid", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[4], (i) -> {
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    openMap();
                })
        );

        registerPhase("AfterObtainDrone", new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[4], (i) -> {
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    openMap();
                })
        );

        registerPhase("Leave", new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[4], (i) -> openMap()));


    }

    public void tryObtain() {
        if ( AbstractDungeon.miscRng.random(0, 99) < baseChance + tmpChance )
            transitionKey("ObtainDrone");
        else {
            tmpChance += tmpChance;
            transitionKey("FailedDrone");
        }
    }
}
