package com.javiluli.smarthopper.client;

import com.javiluli.smarthopper.block.SmartHopperBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Clase encargada de renderizar (dibujar) objetos dinámicos sobre el bloque.
 * Implementa BlockEntityRenderer vinculado a nuestra SmartHopperBlockEntity.
 */
public class SmartHopperRenderer implements BlockEntityRenderer<SmartHopperBlockEntity> {
    // El renderizador de ítems estándar de Minecraft para dibujar la rejilla como un objeto
    private final ItemRenderer itemRenderer;

    public SmartHopperRenderer(BlockEntityRendererProvider.Context context) {
        // Obtenemos el renderizador de ítems del contexto del juego
        this.itemRenderer = context.getItemRenderer();
    }

    /**
     * Método principal de dibujo. Se ejecuta en cada frame si el bloque es visible.
     */
    @SuppressWarnings("null")
	@Override
    public void render(SmartHopperBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        // 1. Obtenemos qué ítem hay en el slot de la rejilla
        ItemStack stack = be.getGridStack();
        
        // Si no hay rejilla puesta, no dibujamos nada y salimos
        if (stack.isEmpty()) return;

        // 2. Iniciamos una nueva transformación (Pose) para no afectar al renderizado de otros bloques
        poseStack.pushPose();
        // 3. Posicionamiento:
        // Movemos el ítem al centro del bloque (0.5, 0.5) y lo elevamos justo encima (1.03)
        poseStack.translate(0.5, 1.03, 0.5); 
        // 4. Escalado: Reducimos un poco el tamaño para que encaje perfectamente en el hueco superior
        poseStack.scale(1.0f, 1.0f, 1.0f);
        // 5. Rotación: Lo giramos 90 grados en el eje X para que quede plano (tumbado) sobre la tolva
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        // 6. Dibujamos el ítem físicamente
        this.itemRenderer.renderStatic(
                stack,                      // El ítem a dibujar (la rejilla)
                ItemDisplayContext.FIXED,   // Contexto de renderizado (ignora gravedad o manos)
                combinedLight,              // La luz ambiental del mundo en esa posición
                combinedOverlay,            // Efectos de superposición (como cuando un bloque se rompe)
                poseStack,                  // Nuestra pila de transformaciones (posición/rotación)
                buffer,                     // El buffer de dibujo
                be.getLevel(),              // El nivel donde se encuentra
                0                           // ID de la entidad (0 para bloques)
        );
        
        // 7. Cerramos la transformación y restauramos la matriz de renderizado original
        poseStack.popPose();
    }
}