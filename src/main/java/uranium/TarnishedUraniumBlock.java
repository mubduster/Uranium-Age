package uranium;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TarnishedUraniumBlock extends Block {
    private static final int RADIATION_RADIUS = 7;

    private static final int TICK_DELAY = 40;

    public TarnishedUraniumBlock(BlockBehaviour.Properties properties) {
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

            boolean aboveBlocked = level.getBlockState(pos.above()).is(ModBlocks.LEAD_BLOCK);
            boolean belowBlocked = level.getBlockState(pos.below()).is(ModBlocks.LEAD_BLOCK);
            boolean northBlocked = level.getBlockState(pos.north()).is(ModBlocks.LEAD_BLOCK);
            boolean southBlocked = level.getBlockState(pos.south()).is(ModBlocks.LEAD_BLOCK);
            boolean eastBlocked = level.getBlockState(pos.east()).is(ModBlocks.LEAD_BLOCK);
            boolean westBlocked = level.getBlockState(pos.west()).is(ModBlocks.LEAD_BLOCK);

            for (LivingEntity entity :targets){
                double diffX = entity.getX() - centerX;
                double diffY = entity.getY() - centerY;
                double diffZ = entity.getZ() - centerZ;

                double absX = Math.abs(diffX);
                double absY = Math.abs(diffY);
                double absZ = Math.abs(diffZ);

                boolean isProtected = false;

                if (absY >= absX && absY >= absZ) {
                    if (diffY > 0 && aboveBlocked) isProtected = true;
                    else if (diffY < 0 && belowBlocked) isProtected = true;
                }
                else if (absX >= absY && absX >= absZ) {
                    if (diffX > 0 && eastBlocked) isProtected = true;
                    else if (diffX < 0 && westBlocked) isProtected = true;
                }
                else {
                    if (diffZ < 0 && northBlocked) isProtected = true;
                    else if (diffZ > 0 && southBlocked) isProtected = true;
                }

                if (!isProtected) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.POISON,
                            30,
                            0,
                            false,
                            false
                    ));
                }
            }
        }

        level.scheduleTick(pos, this, TICK_DELAY);
    }
}
