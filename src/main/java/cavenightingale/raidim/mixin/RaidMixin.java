package cavenightingale.raidim.mixin;

import cavenightingale.raidim.RaidMember;
import cavenightingale.raidim.RaidImMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@Mixin(Raid.class)
public abstract class RaidMixin {
	@Shadow
	private int wavesSpawned;
	@Shadow
	private float totalHealth;
	@Shadow
	private ServerWorld world;
	@Shadow
	private Random random;
	@Shadow
	private Optional<BlockPos> preCalculatedRavagerSpawnLocation;
	@Shadow
	private int waveCount;
	@Shadow
	private BlockPos center;
	@Shadow
	private boolean isSpawningExtraWave() {return false;}
	@Shadow
	private int getBonusCount(Raid.Member member, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {return 0;}
	@Shadow
	private void setWaveCaptain(int wave, RaiderEntity entity) {}
	@Shadow
	private void addRaider(int wave, RaiderEntity raider, @Nullable BlockPos pos, boolean existing) {}
	@Shadow
	private int getMaxWaves(Difficulty difficulty) {return 0;}
	@Shadow
	private void updateBar() {}
	@Shadow
	private void markDirty() {}
	@Shadow
	private int getCount(Raid.Member member, int wave, boolean extra){return 0;}


	/**
	 * @author CaveNightingale
	 * @reason Type changed
	 */
	@Overwrite
	private void spawnNextWave(BlockPos pos) {
		boolean bl = false;
		int i = this.wavesSpawned + 1;
		this.totalHealth = 0.0F;
		LocalDifficulty localDifficulty = this.world.getLocalDifficulty(pos);
		boolean bl2 = this.isSpawningExtraWave();
		Raid.Member[] var6 = Raid.Member.values();
		int var7 = var6.length;

		for(RaidMember member : RaidImMod.MEMBERS) {
			int j = this.getCount(member, i, bl2) + this.getBonusCount(member, this.random, i, localDifficulty, bl2);
			int k = 0;

			for(int l = 0; l < j; ++l) {
				RaiderEntity raiderEntity = (RaiderEntity)member.getEntityType().create(this.world);
				if (!bl && raiderEntity.canLead()) {
					raiderEntity.setPatrolLeader(true);
					this.setWaveCaptain(i, raiderEntity);
					bl = true;
				}

				this.addRaider(i, raiderEntity, pos, false);
				if (member.getEntityType() == EntityType.RAVAGER) {
					RaiderEntity raiderEntity2 = null;
					if (i == this.getMaxWaves(Difficulty.NORMAL)) {
						raiderEntity2 = (RaiderEntity)EntityType.PILLAGER.create(this.world);
					} else if (i >= this.getMaxWaves(Difficulty.HARD)) {
						if (k == 0) {
							raiderEntity2 = (RaiderEntity)EntityType.EVOKER.create(this.world);
						} else {
							raiderEntity2 = (RaiderEntity)EntityType.VINDICATOR.create(this.world);
						}
					}

					k++;
					if (raiderEntity2 != null) {
						this.addRaider(i, raiderEntity2, pos, false);
						raiderEntity2.refreshPositionAndAngles(pos, 0.0F, 0.0F);
						raiderEntity2.startRiding(raiderEntity);
					}
				}
			}
		}

		this.preCalculatedRavagerSpawnLocation = Optional.empty();
		++this.wavesSpawned;
		this.updateBar();
		this.markDirty();
	}

	private int getCount(RaidMember member, int wave, boolean extra) {
		return (Object)member instanceof Raid.Member ?
				getCount((Raid.Member)(Object)member, wave, extra) : member.getCount((Raid)(Object)this, waveCount, wave, extra);
	}

	private int getBonusCount(RaidMember member, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {
		return (Object)member instanceof Raid.Member ?
				getBonusCount((Raid.Member)(Object)member, random, wave, localDifficulty, extra) :
				member.getBonusCount((Raid)(Object)this, random, wave, localDifficulty, extra);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void raidim_onTick(CallbackInfo info) {
		ArrayList<VillagerEntity> villagers = new ArrayList<>();
		int raidRadius = world.getGameRules().getInt(RaidImMod.raidRadius);
		for(Entity e : world.getEntitiesByType(EntityType.VILLAGER, x -> true)) {
			if(e instanceof VillagerEntity) {
				VillagerEntity v = (VillagerEntity)e;
				if(v.getBlockPos().getSquaredDistance(center) <= raidRadius * raidRadius) {
					villagers.add(v);
				}
			}
		}
		double x = 0, y = 0, z = 0;
		if(!villagers.isEmpty()) {
			for(VillagerEntity e : villagers) {
				x += e.getX();
				y += e.getY();
				z += e.getZ();
			}
			x /= villagers.size();/* move to average position*/
			y /= villagers.size();
			z /= villagers.size();
			if(center.getSquaredDistance(x, y, z,true) > 0.06 * raidRadius * raidRadius) {
				x = (2 * x + center.getX()) / 3;
				y = (2 * y + center.getY()) / 3;
				z = (2 * z + center.getZ()) / 3;
				center = new BlockPos(x, y, z);
			}
		}
	}
}
