package spireMapOverhaul.zones.lazer.patches;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.lazer.LaserModifier;
import spireMapOverhaul.zones.lazer.LazerZone;

public class ShopPatch {

    @SpirePatch(clz = ShopScreen.class,
                method = "purchasePurge")
    public static class ChangeGridScreen {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("open"))
                        m.replace("if ( " + ZonePatches.class.getSimpleName() + ".currentZone() instanceof " +
                                LazerZone.class.getSimpleName() + " ) { " +
                                this.getClass().getSimpleName() + ".openGrid();" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                }
            };
        }

        public static void openGrid() {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getAttacks(), 1,"tmp",
                    false, false, true, false);
            if ( !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() ) {
                CardModifierManager.addModifier(AbstractDungeon.gridSelectScreen.selectedCards.get(0),
                        new LaserModifier());
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
        }
    }
}
