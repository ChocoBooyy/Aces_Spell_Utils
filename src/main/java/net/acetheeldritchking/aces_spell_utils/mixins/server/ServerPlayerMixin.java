package net.acetheeldritchking.aces_spell_utils.mixins.server;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.acetheeldritchking.aces_spell_utils.entity.mobs.IKeepInventoryEntity;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Do I even use this? Who knows if I will
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @WrapMethod(method = "restoreFrom")
    public void restoreFrom(ServerPlayer that, boolean keepEverything, Operation<Void> original)
    {
        asu$applyIKeepInvEntityPerks(that);
    }

    @SuppressWarnings("ConstantConditions")
    @Unique
    private void asu$applyIKeepInvEntityPerks(ServerPlayer that)
    {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;

        if (serverPlayer.getLastHurtMob() instanceof IKeepInventoryEntity) {
            serverPlayer.getInventory().replaceWith(that.getInventory());
            serverPlayer.experienceLevel = that.experienceLevel;
            serverPlayer.totalExperience = that.totalExperience;
            serverPlayer.experienceProgress = that.experienceProgress;
            serverPlayer.setScore(that.getScore());
        }
    }
}
