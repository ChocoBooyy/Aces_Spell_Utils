package net.acetheeldritchking.aces_spell_utils.ribbon;

import net.minecraft.util.Mth;

// length counts tick samples, so it sets how long the ribbon lingers rather than how long it is in blocks
public record RibbonConfig(int color, float width, int length, float alpha) {
    public RibbonConfig {
        width = Float.isFinite(width) ? Math.max(0.001F, width) : 0.001F;
        length = Mth.clamp(length, 2, 128);
        alpha = Float.isFinite(alpha) ? Mth.clamp(alpha, 0.0F, 1.0F) : 0.0F;
    }
}
