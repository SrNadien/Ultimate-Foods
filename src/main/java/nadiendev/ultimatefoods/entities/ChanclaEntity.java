package nadiendev.ultimatefoods.entities;

import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ChanclaEntity extends ThrowableItemProjectile {

    private static final int MAX_LIFE = 200;
    private boolean returning = false;
    private int ticksAlive = 0;

    public ChanclaEntity(EntityType<? extends ChanclaEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ChanclaEntity(Level level, LivingEntity thrower) {
        super(ModEntities.CHANCLA.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ToolsAdds.CHANCLA.get();
    }

    @Override
    public void tick() {
        // NO llamar a super.tick() cuando está returning para evitar
        // que la física del proyectil interfiera con el movimiento de retorno
        if (!returning) {
            super.tick();
        } else {
            // Lógica de movimiento manual cuando regresa
            baseTick();
        }

        ticksAlive++;

        Entity owner = getOwner();

        if (returning || ticksAlive > MAX_LIFE) {
            returning = true;

            if (owner != null) {
                Vec3 ownerCenter = owner.position().add(0, owner.getBbHeight() / 2.0, 0);
                Vec3 toOwner = ownerCenter.subtract(position());

                if (toOwner.length() < 1.5) {
                    if (!level().isClientSide && owner instanceof Player player) {
                        ItemStack chanclaStack = new ItemStack(ToolsAdds.CHANCLA.get());
                        if (!player.getInventory().add(chanclaStack)) {
                            player.drop(chanclaStack, false);
                        }
                    }
                    discard();
                    return;
                }

                // Velocidad creciente conforme pasa el tiempo
                double speed = Math.min(0.8 + (ticksAlive * 0.005), 3.0);
                setDeltaMovement(toOwner.normalize().scale(speed));

                // Mover la entidad manualmente
                setPos(position().add(getDeltaMovement()));

            } else {
                discard();
            }
        }

        // Partículas solo en cliente
        if (level().isClientSide) {
            level().addParticle(
                    new ItemParticleOption(ParticleTypes.ITEM, getItem()),
                    getX(), getY(), getZ(),
                    0, 0, 0
            );
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // Ignorar si ya está regresando
        if (returning) return;

        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (target == owner) return;

        if (!level().isClientSide) {
            DamageSource source = (owner instanceof LivingEntity living)
                    ? damageSources().thrown(this, living)
                    : damageSources().thrown(this, this);

            target.hurt(source, 200.0F);
        }

        // Activar retorno SIEMPRE al golpear (servidor y cliente para sincronía)
        returning = true;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // NO llamar a super.onHitBlock() — eso descartaría la entidad
        // Solo activar el retorno
        returning = true;
    }

    @Override
    protected double getDefaultGravity() {
        return returning ? 0.0 : 0.01;
    }
}