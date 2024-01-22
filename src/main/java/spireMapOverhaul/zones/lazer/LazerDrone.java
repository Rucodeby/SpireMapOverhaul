package spireMapOverhaul.zones.lazer;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.abstracts.AbstractSMORelic;

import java.util.List;
import java.util.stream.Collectors;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class LazerDrone extends AbstractSMORelic {

    public static final String ID = makeID("LazerDrone");

    public LazerDrone() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
        counter = 1;
    }

    public LazerDrone(int count) {
        this();
        counter = count;
    }

    @Override
    public void atTurnStartPostDraw() {
        List<AbstractCreature> targets = AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped()).collect(Collectors.toList());
        targets.add(AbstractDungeon.player);
        AbstractCreature t = targets.get(AbstractDungeon.cardRandomRng.random(targets.size() - 1));
        addToBot(new DamageAction(t, new DamageInfo(null, (AbstractDungeon.actNum + GameActionManager.turn) * counter, DamageInfo.DamageType.THORNS)));
    }
}
