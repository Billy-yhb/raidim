package cavenightingale.raidim;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.LocalDifficulty;

import java.util.Random;

public interface RaidMember {
	EntityType<? extends RaiderEntity> getEntityType();
	int getCount(Raid raid, int waveCount, int wave, boolean extra);
	int getBonusCount(Raid raid, Random random, int wave, LocalDifficulty localDifficulty, boolean extra);
}
