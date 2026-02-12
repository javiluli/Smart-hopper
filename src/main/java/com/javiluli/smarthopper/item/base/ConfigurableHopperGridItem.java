package com.javiluli.smarthopper.item.base;

import java.util.List;

import com.javiluli.smarthopper.inventory.HopperGridMenu;
import com.javiluli.smarthopper.register.ModMenuTypes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public abstract class ConfigurableHopperGridItem extends AbstractHopperGridItem {
	public ConfigurableHopperGridItem(Properties props) {
		super(props);
	}

	/**
	 * Se ejecuta cuando el jugador hace click derecho con la rejilla en la mano
	 * (aire o bloque).
	 */
	@SuppressWarnings("null")
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) { // Lógica solo en el Servidor
			// Abrimos la interfaz (GUI) de la rejilla
			player.openMenu(new SimpleMenuProvider((id, inv, p) -> {

				HopperGridMenu menu = new HopperGridMenu(id, inv);
				// Si la rejilla ya tiene un filtro guardado en sus Data Components (1.21)
				if (stack.has(ModMenuTypes.FILTER_ITEM.get())) {
					ItemStack savedFilter = stack.get(ModMenuTypes.FILTER_ITEM.get());
					if (savedFilter != null && !savedFilter.isEmpty()) {
						// Ponemos visualmente ese ítem en el slot de la interfaz al abrirla
						menu.getContainer().setItem(0, savedFilter.copy());
					}
				}
				return menu;
			}, Component.translatable("container.smarthopper.grid_title")));
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	/**
	 * Método de utilidad estático para consultar qué hay dentro de una rejilla. Lo
	 * usa la Tolva para saber qué filtrar.
	 */
	@SuppressWarnings("null")
	public static ItemStack getStoredStack(ItemStack grid) {
	    // Si la rejilla es nula o no tiene el componente, devolvemos vacío
	    if (grid == null || !grid.has(ModMenuTypes.FILTER_ITEM.get())) {
	        return ItemStack.EMPTY;
	    }
	    return grid.get(ModMenuTypes.FILTER_ITEM.get());
	}

	/**
	 * Añade información extra (Lore/Tooltip) cuando pasas el ratón sobre el ítem en
	 * el inventario.
	 */
	@SuppressWarnings("null")
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		ItemStack filter = getStoredStack(stack);
		if (!filter.isEmpty()) {
			tooltip.add(Component.translatable("tooltip.smarthopper.filtering")
					.withStyle(ChatFormatting.GRAY)
					.append(filter.getHoverName().copy().withStyle(ChatFormatting.YELLOW)));
		} else {
			tooltip.add(Component.translatable("tooltip.smarthopper.empty")
					.withStyle(ChatFormatting.DARK_GRAY));
		}
		tooltip.add(Component.translatable("tooltip.smarthopper.info")
				.withStyle(ChatFormatting.GRAY));
	}
	
	// Esto asegura que si tiene datos (filtro), Minecraft lo trate como un objeto
	// único
	@Override
	public boolean canFitInsideContainerItems() {
		return true; // O false si quieres prohibir meter rejillas dentro de mochilas de otros mods
	}

}
