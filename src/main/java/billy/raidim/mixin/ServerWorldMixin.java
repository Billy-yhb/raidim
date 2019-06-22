package billy.raidim.mixin;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World{
	protected ServerWorldMixin(LevelProperties levelProperties_1, DimensionType dimensionType_1,
			BiFunction<World, Dimension, ChunkManager> biFunction_1, Profiler profiler_1, boolean boolean_1) {
		super(levelProperties_1, dimensionType_1, biFunction_1, profiler_1, boolean_1);
	}
	@Shadow
	private RaidManager raidManager;
	@Shadow
	private boolean isNearOccupiedPointOfInterest(BlockPos blockPos_1) {return false;}
	@Overwrite
	public Raid getRaidAt(BlockPos blockPos_1) {
		int r=getGameRules().getInteger("raidRadius");
		Raid raid_1 = this.raidManager.getRaidAt(blockPos_1, r*r*r);
		return this.isNearOccupiedPointOfInterest(blockPos_1) ? raid_1 : null;
	}
}
