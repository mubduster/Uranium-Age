package uranium;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;


public class ReactorBlock extends BaseEntityBlock {
    public ReactorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(GLOWING, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec(){
        return simpleCodec(ReactorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new ReactorBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() &&  level.getBlockEntity(pos) instanceof ReactorBlockEntity reactorBlock){
            player.openMenu(reactorBlock);
        }
        return InteractionResult.SUCCESS;
    }

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, GLOWING);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker getTicker(Level level, BlockState state, BlockEntityType<T> type){
        if(level.isClientSide()){
            return createTickerHelper(type, ModBlockEntities.Reactor_Block_Entity, ReactorBlockEntity::clientTick);
        }
        return createTickerHelper(type, ModBlockEntities.Reactor_Block_Entity, ReactorBlockEntity::tick);
    }
}
