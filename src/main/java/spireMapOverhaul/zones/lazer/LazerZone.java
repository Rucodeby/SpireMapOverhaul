package spireMapOverhaul.zones.lazer;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LazerZone extends AbstractZone implements
        CombatModifyingZone,
        RewardModifyingZone,
        ShopModifyingZone
{

    public static final String ID = "LazerZone";
    private static final int chanceForReward = 70, chanceForCard = 20;

    public LazerZone () {
        super(ID, Icons.SHOP);
        width = 3;
        height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new LazerZone();
    }

    // Modify cards with lazer
    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        if ( AbstractDungeon.cardRng.random(0, 99) < chanceForReward ) {
            for (AbstractCard card : cards) {
                if ( card.type == AbstractCard.CardType.ATTACK && AbstractDungeon.cardRng.random(0, 99) < chanceForCard )
                    CardModifierManager.addModifier(card, new LaserModifier());
            }
        };
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void atRoundEnd() {
        List<AbstractCreature> targets = AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped()).collect(Collectors.toList());
        targets.add(AbstractDungeon.player);
        AbstractCreature t = targets.get(AbstractDungeon.cardRandomRng.random(targets.size() - 1));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(t, new DamageInfo(null, AbstractDungeon.actNum + GameActionManager.turn, DamageInfo.DamageType.THORNS)));
    }
}
