package cavenightingale.raidim;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.LocalDifficulty;

import java.util.Random;

public final class CustomRaidMemberImpl implements RaidMember{
	@FunctionalInterface
	public interface BonusCountGetter {
		int accept(Raid raid, Random random, int wave, LocalDifficulty localDifficulty, boolean extra);
		BonusCountGetter DEFAULT = (unused1, unused2, unused3, unused4, unused5) -> 0;
	}

	private EntityType<? extends RaiderEntity> type;
	private BonusCountGetter bonusCountGetter;
	public int[] countInWaves;

	public CustomRaidMemberImpl(EntityType<? extends RaiderEntity> type, int[] countInWaves, BonusCountGetter bonusCountGetter) {
		if(countInWaves.length != 8)
			throw new IllegalArgumentException("countInWaves must be int[8]");
		this.type = type;
		this.countInWaves = countInWaves;
		this.bonusCountGetter = bonusCountGetter;
	}

	public CustomRaidMemberImpl(EntityType<? extends RaiderEntity> type, int[] countInWaves) {
		this(type, countInWaves, BonusCountGetter.DEFAULT);
	}

	@Override
	public EntityType<? extends RaiderEntity> getEntityType() {
		return type;
	}

	@Override
	public int getCount(Raid raid, int waveCount, int wave, boolean extra) {
		return extra ? countInWaves[waveCount] : countInWaves[wave];
	}

	@Override
	public int getBonusCount(Raid raid, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {
		return bonusCountGetter.accept(raid, random, wave, localDifficulty, extra);
	}
}
