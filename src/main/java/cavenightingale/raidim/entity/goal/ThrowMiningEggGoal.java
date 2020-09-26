package cavenightingale.raidim.entity.goal;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.entity.MinerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.raid.Raid;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static net.minecraft.entity.ai.goal.Goal.Control.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Control.MOVE;

public class ThrowMiningEggGoal extends Goal {
	private final MinerEntity minerEntity;
	private final EnumSet<Control> DEFAULT = EnumSet.noneOf(Control.class);
	private final EnumSet<Control> ACTIVE = EnumSet.of(LOOK);
	private final EnumSet<Control> GOLEM = EnumSet.of(LOOK, MOVE);
	public ThrowMiningEggGoal(MinerEntity minerEntity) {
		this.minerEntity = minerEntity;
		setControls(DEFAULT);
	}

	@Override
	public boolean canStart() {
		return getTargetPos() != null;
	}

	@Override
	public boolean shouldContinue() {
		return (!minerEntity.canThrow() && minerEntity.getMiningEggCD() <= 90) || getTargetPos() != null;
	}

	@Override
	public void start() {
		super.start();
		minerEntity.setShowPickaxe(true);
	}

	@Override
	public void tick() {
		super.tick();
		if(minerEntity.getTarget() instanceof IronGolemEntity)
			setControls(GOLEM);
		else if(minerEntity.getTarget() != null)
			setControls(ACTIVE);
		else
			setControls(DEFAULT);
		Vec3d tgpos = getTargetPos();
		if(minerEntity.canThrow())
			minerEntity.setMiningEggCD(90);
		if(minerEntity.getMiningEggCD() == 60 && tgpos != null)
			minerEntity.throwMiningEgg(tgpos);
		if(tgpos != null)
			minerEntity.getLookControl().lookAt(tgpos);
		if(minerEntity.getTarget() instanceof IronGolemEntity &&
				minerEntity.canSee(minerEntity.getTarget()) && minerEntity.distanceTo(minerEntity.getTarget()) < 20.0d)
			minerEntity.getNavigation().stop();
		else if(minerEntity.getTarget() instanceof IronGolemEntity)
			minerEntity.getNavigation().startMovingTo(minerEntity.getTarget(), 1.0d);
	}

	@Override
	public void stop() {
		super.stop();
		minerEntity.setShowPickaxe(false);
	}

	@Nullable
	public Vec3d getTargetPos() {
		LivingEntity target = minerEntity.getTarget();
		if(target instanceof GolemEntity)
			return target.getPos();

		Path path = minerEntity.getNavigation().getCurrentPath();

		if((path != null && path.getLength() >= 15.0 * Math.sqrt(minerEntity.squaredDistanceTo(Vec3d.of(path.getTarget())))) ||
				(path == null && target != null && !minerEntity.getNavigation().startMovingTo(target, 1.0)))
			return Vec3d.ofCenter(target.getBlockPos());
		if(minerEntity.getRaid() != null && target == null) {
			Raid raid = minerEntity.getRaid();
			Vec3d center = Vec3d.of(raid.getCenter());
			VillagerEntity e = minerEntity.world.getClosestEntity(VillagerEntity.class,
					new TargetPredicate().ignoreEntityTargetRules(),
					null, center.x, center.y, center.z,
					new Box(center, center).expand(minerEntity.world.getGameRules().getInt(RaidImMod.raidRadius)));
			return e.getPos();
		}
		return null;
	}
}
