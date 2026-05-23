package uranium;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RawUraniumBlock extends Block {

    private static final int RADIATION_RADIUS = 9;

    private static final int TICK_DELAY = 40;

    public RawUraniumBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldstate, boolean moved){
        super.onPlace(state,level,pos,oldstate,moved);
        level.scheduleTick(pos, this, TICK_DELAY);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        if (!level.isClientSide()){
            AABB radiationZone = new AABB(pos).inflate(RADIATION_RADIUS);

            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, radiationZone);

            double centerX = pos.getX()+0.5;
            double centerY = pos.getY()+0.5;
            double centerZ = pos.getZ()+0.5;

            for (LivingEntity entity :targets){
                if (entity.distanceToSqr(centerX,centerY,centerZ) <= (RADIATION_RADIUS * RADIATION_RADIUS)) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.POISON,
                            50,
                            1,
                            false,
                            false
                    ));
                }
            }
        }

        level.scheduleTick(pos, this, TICK_DELAY);
    }
}
