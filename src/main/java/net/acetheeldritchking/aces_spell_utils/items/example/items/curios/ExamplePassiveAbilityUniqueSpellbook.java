package net.acetheeldritchking.aces_spell_utils.items.example.items.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.acetheeldritchking.aces_spell_utils.items.curios.PassiveAbilityUniqueSpellbook;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class ExamplePassiveAbilityUniqueSpellbook extends PassiveAbilityUniqueSpellbook {
    public ExamplePassiveAbilityUniqueSpellbook() {
        super(SpellDataRegistryHolder.of(
                new SpellDataRegistryHolder(SpellRegistry.BLOOD_SLASH_SPELL, 5),
                new SpellDataRegistryHolder(SpellRegistry.BLOOD_STEP_SPELL, 5),
                new SpellDataRegistryHolder(SpellRegistry.RAY_OF_SIPHONING_SPELL, 5),
                new SpellDataRegistryHolder(SpellRegistry.BLAZE_STORM_SPELL, 5)
        ), 6, new Properties().fireResistant().stacksTo(1).rarity(ASRarities.ARID_RARITY_PROXY.getValue()));
        withSpellbookAttributes(new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    protected int getCooldownTicks() {
        return 100;
    }
}
