package uranium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class UraniumBlock extends Block {
//---------------------setup-code------------------------------------------------------------------------------------------------------------
    private static final int RADIATION_RADIUS = 8;

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
        if ( random.nextFloat() < 0.04688f){
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

            boolean northBlocked = level.getBlockState(pos.north()).is(ModBlocks.LEAD_BLOCK);
            boolean southBlocked = level.getBlockState(pos.south()).is(ModBlocks.LEAD_BLOCK);
            boolean eastBlocked = level.getBlockState(pos.east()).is(ModBlocks.LEAD_BLOCK);
            boolean westBlocked = level.getBlockState(pos.west()).is(ModBlocks.LEAD_BLOCK);
            boolean aboveBlocked = level.getBlockState(pos.above()).is(ModBlocks.LEAD_BLOCK);
            boolean belowBlocked = level.getBlockState(pos.below()).is(ModBlocks.LEAD_BLOCK);


            for (LivingEntity entity : targets){
                double diffX = entity.getX() - centerX;
                double diffY = entity.getEyeY() - centerY;
                double diffZ = entity.getZ() - centerZ;

                double absX = Math.abs(diffX);
                double absY = Math.abs(diffY);
                double absZ = Math.abs(diffZ);

                boolean isProtected = false;

                boolean isWearingLeadArmor = false;
                if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.LEAD_HELMET) && entity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.LEAD_CHESTPLATE) && entity.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.LEAD_LEGGINGS) && entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.LEAD_BOOTS)){
                    isWearingLeadArmor = true;
                }

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

                if (!isProtected && !isWearingLeadArmor){
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
