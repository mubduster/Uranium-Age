package uranium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class UraniumBlock extends Block {
//---------------------setup-code------------------------------------------------------------------------------------------------------------
    private static final int RADIATION_RADIUS = 12;

    private static final int TICK_DELAY = 40;

    private final UraniumState age;

    public UraniumBlock(UraniumState age,Properties properties) {
        super(properties);
        this.age = age;
    }
//-------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------degradation-code--------------------------------------------------------------------------------------------------
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.age == UraniumState.FRESH;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        if ( random.nextFloat() < 0.05688f){
            this.tryDegrade(state, level, pos, random);
        }
    }

    private void tryDegrade(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        for(Direction direction : Direction.values()){
            if ( level.getBlockState(pos.relative(direction)).isAir()){
                this.forceDegrade(level, pos);
            }
        }

        int radius = 2;
        int tarnishedNeighbors = 0;

        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))){
            int distance = Math.abs(checkPos.getX() - pos.getX()) + Math.abs(checkPos.getY() - pos.getY()) + Math.abs(checkPos.getZ() - pos.getZ());
            if (distance <= radius && !checkPos.equals(pos)){
                BlockState neighborState = level.getBlockState(checkPos);
                if(neighborState.getBlock() instanceof UraniumBlock neighborBlock){
                    if(neighborBlock.age == UraniumState.TARNISHED){
                        tarnishedNeighbors++;
                    }
                }
            }
        }
        if (tarnishedNeighbors >= 2){
            this.forceDegrade(level, pos);
        }
    }
    private void forceDegrade(Level level, BlockPos pos){
        this.getDegradationResult().ifPresent(nextBlock -> level.setBlock(pos, nextBlock.defaultBlockState(),3));
    }
    private Optional<Block> getDegradationResult(){
     if(this.age == UraniumState.FRESH){
         return Optional.of(ModBlocks.TARNISHED_URANIUM_BLOCK);
     }
     return Optional.empty();
    }
//-------------------------------------------------------------------------------------------------------------------------------------
//------------------radiation-code-----------------------------------------------------------------------------------------------------
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
                            2,
                            false,
                            false
                    ));
                }
            }
        }

        level.scheduleTick(pos, this, TICK_DELAY);
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------
