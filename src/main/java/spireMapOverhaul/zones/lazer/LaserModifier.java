package spireMapOverhaul.zones.lazer;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class LaserModifier extends AbstractCardModifier {

    public static final String ID = makeID("LaserMod");
    public static final Texture icon = new Texture("");

    public int uppDamage = 3;

    public LaserModifier() {
        priority = 2;
    }

    @Override
    public float modifyBaseDamage(float dmg, DamageInfo.DamageType type, AbstractCard c, AbstractMonster m) {
        return dmg + uppDamage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LaserModifier();
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(icon).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(icon).render(card);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            LaserModifier mod = (LaserModifier) CardModifierManager.getModifiers(card, ID).get(0);
            mod.uppDamage += uppDamage;
            card.initializeDescription();
            return false;
        }
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
