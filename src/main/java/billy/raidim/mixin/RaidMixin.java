package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import billy.raidim.RaidImMod;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import net.minecraft.world.World;

@Mixin(VillagerEntity.class)
public abstract class RaidMixin extends AbstractTraderEntity{
	private RaidMixin(EntityType<? extends AbstractTraderEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}
	@Shadow
	private VillagerGossips gossip;
	@Inject(method="tick",at=@At("HEAD"))
	public void onTick(CallbackInfo info) {
		try {
			if(isAlive()&&!world.isClient) {
				for(PlayerEntity player:world.getPlayers()) {
					if(player.distanceTo(this)<64&&player.canSee(this)) {
						int level=0;
						StatusEffectInstance hero=player.getStatusEffect(
								StatusEffects.HERO_OF_THE_VILLAGE);
						if(hero==null)
							continue;
						if((level=hero.getAmplifier())>=2){
							if(Math.random()<0.008*level*level*level/player.squaredDistanceTo(this)) {
								gossip.startGossip(player.getUuid(), 
										VillageGossipType.MINOR_POSITIVE, 30);
							}
						}
					}
				}
			}
		}catch(Throwable t) {
			RaidImMod.logger.catching(t);
		}
	}
}
