package billy.raidim.mixin;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.Tickable;

@Mixin(BellBlockEntity.class)
public abstract class BellBlockEntityMixin extends BlockEntity
	implements Tickable{
	public BellBlockEntityMixin(BlockEntityType<?> blockEntityType_1) {
		super(blockEntityType_1);
	}
	@Shadow
	private List<LivingEntity> field_19156;
	@Overwrite
	public boolean method_20518(LivingEntity livingEntity_1) {
		return livingEntity_1.isAlive() && !livingEntity_1.removed &&
				this.getPos().isWithinDistance(livingEntity_1.getPos(), 64) &&
				livingEntity_1  instanceof RaiderEntity;
	}
	@Overwrite
	private boolean method_20523() {
		Iterator<LivingEntity> var2 = this.field_19156.iterator();
		LivingEntity livingEntity_1;
		do {
			if (!var2.hasNext()) {
				return false;
			}
			livingEntity_1 = (LivingEntity)var2.next();
		} while(!method_20518(livingEntity_1));
		return true;
	}
}
