package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import billy.raidim.GameHooks;
import billy.raidim.GameHooks.ServerLivingEntityHurtHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
	public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}
	@Inject(method="damage",at=@At("RETURN"))
	public void onDamage(DamageSource src, float amount,CallbackInfoReturnable<Boolean> info) {
		for(ServerLivingEntityHurtHook hook:
			GameHooks.server_living_entity_hurt_hooks) {
			hook.onLivingHurt((LivingEntity)((Object)this),src,amount);
		}
	}
}
