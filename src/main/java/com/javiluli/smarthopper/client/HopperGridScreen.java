package com.javiluli.smarthopper.client;

import com.javiluli.smarthopper.SmartHopperMod;
import com.javiluli.smarthopper.inventory.HopperGridMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Esta clase dibuja la interfaz gráfica (la ventana) en la pantalla del
 * jugador. Hereda de AbstractContainerScreen vinculándose a nuestro
 * HopperGridMenu.
 */
public class HopperGridScreen extends AbstractContainerScreen<HopperGridMenu> {
	private final static String TEXTURE_URL = "textures/gui/container/hopper_grid_gui.png";

	// Ubicación de la imagen .png de la interfaz dentro de los recursos del mod
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SmartHopperMod.MODID, TEXTURE_URL);

	public HopperGridScreen(HopperGridMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		// Dimensiones estándar de una ventana de inventario en Minecraft
		this.imageWidth = 176;
		this.imageHeight = 166;
		// Ajusta la posición del texto "Inventario" para que no se solape con los slots
		this.inventoryLabelY = this.imageHeight - 94;
	}

	/**
	 * Dibuja la capa de fondo (la textura de la ventana).
	 */
	@SuppressWarnings("null")
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		// Calculamos el centro de la pantalla para que la ventana quede alineada
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		// blit dibuja la textura: (Textura, X, Y, U, V, Ancho, Alto)
		// El U y V (0,0) indican que empezamos a recortar desde la esquina superior
		// izquierda del PNG
		graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
	}

	/**
	 * Método principal de renderizado que se ejecuta en cada frame.
	 */
	@SuppressWarnings("null")
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		// 1. Dibuja el fondo oscurecido detrás de la ventana (para que el juego no
		// distraiga)
		this.renderBackground(graphics, mouseX, mouseY, partialTick);
		// 2. Llama a renderBg y dibuja los ítems/slots
		super.render(graphics, mouseX, mouseY, partialTick);
		// 3. Dibuja el nombre del objeto (Tooltip) cuando pasas el ratón sobre un ítem
		this.renderTooltip(graphics, mouseX, mouseY);
	}

}