package billy.raidim.entity.goal;

import java.util.EnumSet;

import billy.raidim.entity.MinerEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.raid.Raid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;

public class ThrowDiggingEggGoal extends Goal{
	public MinerEntity miner;
	public ThrowDiggingEggGoal(MinerEntity miner) {
		this.miner=miner;
		setControls(EnumSet.of(Control.LOOK));
	}
	VillagerEntity tg=null;
	BlockPos pos=null;
	Raid raid=null;
	@Override
	public boolean canStart() {
		raid=null;
		return miner.preThrowEgg==0&&miner.diggingeggcooldown==0&&
				miner.world.getGameRules().getBoolean("mobGriefing")&&
				((tg=miner.world.getClosestEntity(VillagerEntity.class,
						new TargetPredicate().includeHidden()
						.includeInvulnerable().includeTeammates()
						.ignoreEntityTargetRules().setBaseMaxDistance(
								miner.getAttributeInstance(
										EntityAttributes.FOLLOW_RANGE).getValue()),
						miner, miner.x, miner.y, miner.z,
						miner.getBoundingBox().expand(
								2*miner.world.getGameRules()
								.getInteger("raidRadius"))))!=null&&
								miner.getTarget()==null)||
				((raid=miner.getRaid())!=null&&miner.getTarget()==null&&
				(tg=miner.world.getClosestEntity(VillagerEntity.class,
						new TargetPredicate().includeHidden()
						.includeInvulnerable().includeTeammates()
						.ignoreEntityTargetRules().setBaseMaxDistance(
								2*miner.world.getGameRules()
								.getInteger("raidRadius")),
						miner, (pos=raid.getCenter()).getX(),
						pos.getY(), pos.getZ(),
						new BoundingBox(raid.getCenter()).expand(
								2*miner.world.getGameRules()
								.getInteger("raidRadius"))))!=null);
	}
	public boolean shouldContinue() {
		return miner.preThrowEgg!=0;
	}
	public void start() {
		miner.preThrowDiggingegg(pos=new BlockPos(tg));
		if(raid!=null) {
			miner.preThrowEgg+=90;
		}
	}
	public void tick() {
		miner.getLookControl().lookAt(pos.getX(),pos.getY(),pos.getZ(), 20, 20);
	}
}
