package net.acetheeldritchking.aces_spell_utils.items.example.items.misc;

import net.acetheeldritchking.aces_spell_utils.ribbon.RibbonConfig;
import net.acetheeldritchking.aces_spell_utils.utils.RibbonHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExampleRibbonItem extends Item {
    public ExampleRibbonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            if (RibbonHandler.isAttached(player)) {
                RibbonHandler.detach(player);
            } else {
                RibbonHandler.attach(player, new RibbonConfig(0x66E0FF, 0.18F, 24, 1.0F));
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
