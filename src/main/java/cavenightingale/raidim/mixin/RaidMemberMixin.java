package cavenightingale.raidim.mixin;

import cavenightingale.raidim.RaidMember;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.Raid.Member;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(Member.class)
public abstract class RaidMemberMixin implements RaidMember {
	@Shadow
	private EntityType<? extends RaiderEntity> type;
	@Shadow
	private int[] countInWave;

	@Override
	public EntityType<? extends RaiderEntity> getEntityType() {
		return type;
	}

	@Override
	public int getCount(Raid raid,  int waveCount, int wave, boolean extra) {
		throw new AssertionError("This should not happen");
	}

	@Override
	public int getBonusCount(Raid raid, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {
		throw new AssertionError("This should not happen");
	}
}
