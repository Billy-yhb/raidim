package cavenightingale.raidim.mixin;

import cavenightingale.raidim.RaidImMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerGossips;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends AbstractTraderEntity implements InteractionObserver, VillagerDataContainer {
	@Shadow
	private VillagerGossips gossip;
	@Shadow
	private void sayNo() {}

	public VillagerEntityMixin(EntityType<? extends AbstractTraderEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method="tick",at=@At("HEAD"))
	private void raidim_onTick(CallbackInfo info) {
		if(isAlive() && !world.isClient) {
			for(PlayerEntity player : world.getPlayers()) {
				if(player.distanceTo(this) < 64 && player.canSee(this)) {
					int level=0;
					StatusEffectInstance hero = player.getStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
					if(hero == null)
						continue;
					if((level = hero.getAmplifier()) >= 2)
						if(Math.random() < 0.008 * level * level * level / player.squaredDistanceTo(this))
							gossip.startGossip(player.getUuid(), VillageGossipType.MINOR_POSITIVE, 100);
				}
			}
		}
	}

	@Inject(method="interactMob",at=@At("HEAD"),cancellable=true)
	private void raidim_onInteractMob(PlayerEntity playerEntity_1, Hand hand_1, CallbackInfoReturnable<Boolean> info) {
		ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
		boolean boolean_1 = itemStack_1.getItem() == Items.NAME_TAG;
		if (boolean_1) {
			itemStack_1.useOnEntity(playerEntity_1, this, hand_1);
		} else if (itemStack_1.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
			if(!world.isClient) {
				if(((ServerWorld)world).getRaidAt(getBlockPos()) != null) {
					if(!world.getGameRules().getBoolean(RaidImMod.tradeInRaid)) {
						sayNo();
						info.setReturnValue(true);
					}
				}
			}
		}
	}

	@Inject(method = "summonGolem",at = @At("HEAD"),cancellable = true)
	private void raidim_onSummonGolem(CallbackInfo info) {
		if(!world.isClient)
			if(((ServerWorld)world).getRaidAt(new BlockPos(this.getPos())) != null)
				if(!world.getGameRules().getBoolean(RaidImMod.villagerSpawnGolemInRaid))
					info.cancel();
			else if(!world.getGameRules().getBoolean(RaidImMod.villagerSpawnGolemCommon))
					info.cancel();
	}
}
