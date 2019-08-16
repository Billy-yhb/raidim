package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class RaiderEntityMixin {
	@Shadow
	private World world;
	@Shadow
	private double x;
	@Shadow
	private double y;
	@Shadow
	private double z;
	@Shadow
	private BoundingBox getBoundingBox() {return null;}
	@Shadow
	private int fireTime;
	@Shadow
	private void setOnFireFor(int i) {}
	@Shadow
	public boolean damage(DamageSource d, float f) {return false;}
	@Overwrite
	public void onStruckByLightning(LightningEntity lightning) {
		if(((Object)this) instanceof RaiderEntity) {
			return;
		}
		++this.fireTime;
		if (this.fireTime == 0) {
			this.setOnFireFor(8);
		}

		this.damage(DamageSource.LIGHTNING_BOLT, 8.0F);
	}
}
