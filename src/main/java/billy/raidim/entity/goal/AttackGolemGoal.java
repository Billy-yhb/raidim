package billy.raidim.entity.goal;

import java.util.EnumSet;

import billy.raidim.entity.MinerEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.BlockPos;

public class AttackGolemGoal extends Goal{
	public MinerEntity miner;
	public AttackGolemGoal(MinerEntity miner) {
		this.miner=miner;
		setControls(EnumSet.of(Control.LOOK));
	}
	@Override
	public boolean canStart() {
		IronGolemEntity tg=miner.world.getClosestEntity(IronGolemEntity.class,
				new TargetPredicate().includeHidden()
				.includeInvulnerable().includeTeammates()
				.ignoreEntityTargetRules().setBaseMaxDistance(
						miner.getAttributeInstance(
								EntityAttributes.FOLLOW_RANGE).getValue()*2),
				miner, miner.x, miner.y, miner.z,
				miner.getBoundingBox().expand(
						2*miner.world.getGameRules()
						.getInteger("raidRadius")));
		if(tg==null||!miner.canSee(tg)) {
			return false;
		}else {
			miner.setTarget(tg);
		}
		return miner.preThrowEgg==0&&miner.diggingeggcooldown==0;
	}
	public boolean shouldContinue() {
		return miner.preThrowEgg!=0;
	}
	BlockPos pos=null;
	public void start() {
		miner.preThrowDiggingegg(pos=miner.getTarget().getBlockPos());
	}
	public void stop() {
		miner.diggingeggcooldown-=60;
	}
	public void tick() {
		miner.getLookControl().lookAt(pos.getX(),pos.getY(),pos.getZ(), 20, 20);
		if(miner.preThrowEgg>=3)
			miner.targetpos=pos=miner.getTarget().getBlockPos();
	}
}
