package com.javiluli.smarthopper.inventory;

import com.javiluli.smarthopper.item.base.ConfigurableHopperGridItem;
import com.javiluli.smarthopper.register.ModMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Gestiona la lógica de slots tipo "Fantasma" para la rejilla.
 */
public class HopperGridMenu extends AbstractContainerMenu {
	private final SimpleContainer container = new SimpleContainer(1);

	public Container getContainer() {
		return this.container;
	}

	public HopperGridMenu(int id, Inventory inv, net.minecraft.network.FriendlyByteBuf extraData) {
		this(id, inv);
	}

	@SuppressWarnings("null")
	public HopperGridMenu(int id, Inventory inv) {
		super(ModMenuTypes.HOPPER_GRID_MENU.get(), id);

		// SLOT FANTASMA (Slot 0)
		this.addSlot(new Slot(this.container, 0, 80, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return true; // Permite cualquier item
			}

			@Override
			public void set(ItemStack stack) {
				// Si el stack no está vacío, guardamos una COPIA de 1 unidad
				if (!stack.isEmpty()) {
					super.set(stack.copyWithCount(1));
				} else {
					super.set(ItemStack.EMPTY);
				}
			}

			@Override
			public boolean mayPickup(Player playerIn) {
				// Al intentar recogerlo, simplemente limpiamos el slot
				this.set(ItemStack.EMPTY);
				return false; // No permite "llevarse" el item fantasma
			}
		});

		layoutPlayerInventorySlots(inv, 8, 51);
	}

	/**
	 * IMPORTANTE: Para items fantasmas, debemos interceptar el click del ratón
	 */
	@SuppressWarnings("null")
	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		// Si el jugador hace click en el slot del filtro (id 0)
		if (slotId == 0) {
			ItemStack cursorStack = this.getCarried();
			Slot targetSlot = this.slots.get(0);

			if (clickType == ClickType.QUICK_MOVE) { // Shift + Click sobre el slot de filtro
				targetSlot.set(ItemStack.EMPTY);
			} else {
				// Ponemos una copia de lo que haya en el cursor
				targetSlot.set(cursorStack.copy());
			}
			// Devolvemos el resultado sin que Minecraft reste items del cursor
			return;
		}
		super.clicked(slotId, button, clickType, player);
	}

	@SuppressWarnings("null")
	private void layoutPlayerInventorySlots(Inventory inv, int x, int y) {
		int playerInvY = 84;
		int hotbarY = 142;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, playerInvY + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inv, i, 8 + i * 18, hotbarY));
		}
	}

	@SuppressWarnings("null")
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		// En un menú de filtro fantasma, el Shift+Click desde el inventario
		// solo debe copiar el item, no moverlo.
		if (index >= 1) { // Click desde el inventario del jugador
			Slot playerSlot = this.slots.get(index);
			if (playerSlot != null && playerSlot.hasItem()) {
				ItemStack stackInPlayerSlot = playerSlot.getItem();
				// Ponemos una copia en el filtro
				this.slots.get(0).set(stackInPlayerSlot.copy());
			}
		} else if (index == 0) { // Shift+Click en el filtro para borrarlo
			this.slots.get(0).set(ItemStack.EMPTY);
		}
		return ItemStack.EMPTY; // Importante: devolvemos EMPTY para que no "mueva" nada realmente
	}

	@SuppressWarnings("null")
	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@SuppressWarnings("null")
	@Override
	public void removed(Player player) {
        super.removed(player);

        // 1. Localizar qué stack está usando el jugador
        ItemStack handStack = player.getMainHandItem();
        // CAMBIO AQUÍ: Usamos la clase base "Configurable"
        if (!(handStack.getItem() instanceof ConfigurableHopperGridItem)) {
            handStack = player.getOffhandItem();
        }

        // CAMBIO AQUÍ: Volvemos a usar la clase base para que acepte la Inversa
        if (handStack.getItem() instanceof ConfigurableHopperGridItem) {
            ItemStack filterStack = this.container.getItem(0);

            if (filterStack.isEmpty()) {
                handStack.remove(ModMenuTypes.FILTER_ITEM.get());
            } else {
                if (handStack.getCount() > 1) {
                    handStack.shrink(1);
                    ItemStack singleGridWithFilter = new ItemStack(handStack.getItem(), 1);
                    singleGridWithFilter.set(ModMenuTypes.FILTER_ITEM.get(), filterStack.copy());

                    if (!player.getInventory().add(singleGridWithFilter)) {
                        player.drop(singleGridWithFilter, false);
                    }
                } else {
                    handStack.set(ModMenuTypes.FILTER_ITEM.get(), filterStack.copy());
                }
            }
        }
    }
}