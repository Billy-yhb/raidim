package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
	@Overwrite
	public boolean method_20518(LivingEntity livingEntity_1) {
		return livingEntity_1.isAlive() && !livingEntity_1.removed &&
				this.getPos().isWithinDistance(livingEntity_1.getPos(), 64) &&
				livingEntity_1  instanceof RaiderEntity;
	}
}
