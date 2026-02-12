package com.javiluli.smarthopper.item.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Clase base para todas las rejillas de filtrado.
 */
public abstract class AbstractHopperGridItem extends Item {

	public AbstractHopperGridItem(Properties properties) {
		super(properties);
	}

	/**
	 * El método que cada rejilla DEBE implementar con su propia lógica.
	 * 
	 * @param gridStack El ItemStack de la rejilla (para leer sus datos si los
	 *                  tiene)
	 * @param candidate El ítem que quiere entrar en la tolva
	 */
	public abstract boolean canItemPass(ItemStack gridStack, ItemStack candidate);
}