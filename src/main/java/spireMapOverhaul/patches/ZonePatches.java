package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;

//For basically all zone hooks.
public class ZonePatches {
    public static AbstractZone currentZone() {
        return AbstractDungeon.currMapNode == null ? null : Fields.zone.get(AbstractDungeon.currMapNode);
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = SpirePatch.CLASS
    )
    public static class Fields {
        public static SpireField<AbstractZone> zone = new SpireField<>(()->null);
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "render"
    )
    public static class TempDisplay {
        @SpirePrefixPatch
        public static void setColor(MapRoomNode __instance, SpriteBatch sb) {
            AbstractZone zone = Fields.zone.get(__instance);
            if (zone != null) {
                __instance.color = zone.getAdjustedColor().cpy();
            }
        }
    }

    @SuppressWarnings("unused")
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class ZoneRenderPatches {
        @SpirePrefixPatch
        public static void renderBackground(SpriteBatch sb) {
            AbstractZone zone = Fields.zone.get(AbstractDungeon.currMapNode);
            if (zone instanceof RenderableZone) {
                ((RenderableZone) zone).renderBackground(sb);
            }
        }

        @SpirePostfixPatch
        public static void renderForeground(SpriteBatch sb) {
            AbstractZone zone = Fields.zone.get(AbstractDungeon.currMapNode);
            if (zone instanceof RenderableZone) {
                ((RenderableZone) zone).renderForeground(sb);
            }
        }

        @SpireInsertPatch(
                locator = PostRenderBackgroundLocator.class
        )
        public static void PostRenderBackground(SpriteBatch sb) {
            AbstractZone zone = Fields.zone.get(AbstractDungeon.currMapNode);
            if (zone instanceof RenderableZone) {
                ((RenderableZone) zone).postRenderBackground(sb);
            }
        }

        private static class PostRenderBackgroundLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "render");
                return new int[]{LineFinder.findInOrder(ctBehavior, finalMatcher)[0] - 1};
            }
        }

    }

    @SuppressWarnings("unused")
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "update"
    )
    public static class ZoneUpdatePatches {
        @SpirePrefixPatch
        public static void updateZone() {
            AbstractZone zone = Fields.zone.get(AbstractDungeon.currMapNode);
            if (zone instanceof RenderableZone) {
                ((RenderableZone) zone).update();
            }
        }
    }
}
