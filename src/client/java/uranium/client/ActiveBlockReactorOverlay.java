package uranium.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import uranium.ReactorBlock;
import uranium.ReactorBlockEntity;

public class ActiveBlockReactorOverlay implements BlockEntityRenderer<ReactorBlockEntity, ReactorRenderState> {
    public ActiveBlockReactorOverlay (BlockEntityRendererProvider.Context ctx){}

    @Override
    public ReactorRenderState createRenderState(){
        return new ReactorRenderState();
    }

    @Override
    public void extractRenderState(ReactorBlockEntity entity, ReactorRenderState state, float partialTick, net.minecraft.world.phys.Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress){
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTick, cameraPosition, breakProgress);

        Level level = entity.getLevel();
        if (level == null) return;

        BlockPos pos = entity.getBlockPos();
        BlockState reactorState = level.getBlockState(pos);

        state.active = reactorState.getValue(ReactorBlock.ACTIVE);
        state.furnaceAbove = level.getBlockState(pos.above()).getBlock() instanceof AbstractFurnaceBlock;
    }

    @Override
    public void submit (ReactorRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera){
        if (!state.active || !state.furnaceAbove) return;

        poseStack.pushPose();
        poseStack.translate(0, 1, 0);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
                    Matrix4f matrix = pose.pose();
                    drawColoredBox(matrix, buffer, 5.0f, 0.0f, 0.0f, 0.05f);
                });
        poseStack.popPose();
    }

    private void drawColoredBox (Matrix4f matrix, VertexConsumer consumer, float r, float g, float b, float a){
        float c = 0.002f;
        float min = -c;
        float max = 1f + c;
        quad(consumer, matrix, min,min,max, max,min,min, max,min,min, min,min,min, r,g,b,a);
        quad(consumer, matrix, min,max,min, max,max,min, max,max,max, min,max,max, r,g,b,a);
        quad(consumer, matrix, max,max,min, min,max,min, min,min,min, max,min,min, r,g,b,a);
        quad(consumer, matrix, min,max,max, max,max,max, max,min,max, min,min,max, r,g,b,a);
        quad(consumer, matrix, min,max,min, min,max,max, min,min,max, min,min,min, r,g,b,a);
        quad(consumer, matrix, max,max,max, max,max,min, max,min,min, max,min,max, r,g,b,a);
    }

    private void quad (VertexConsumer c, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b ,float a){
        c.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
        c.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
        c.addVertex(m, x3, y3, z3).setColor(r, g, b, a);
        c.addVertex(m, x4, y4, z4).setColor(r, g, b, a);
    }

    @Override
    public boolean shouldRenderOffScreen(){
        return true;
    }
}
