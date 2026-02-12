package com.javiluli.smarthopper.block;

import com.javiluli.smarthopper.item.base.AbstractHopperGridItem;
import com.javiluli.smarthopper.register.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SmartHopperBlockEntity extends HopperBlockEntity {
	private ItemStack gridStack = ItemStack.EMPTY;

	public SmartHopperBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntities.SMART_HOPPER_BE.get();
	}

	// --- SINCRONIZACIÓN RED (Para que se vea la rejilla) ---

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		// Envía un paquete de datos al cliente cuando el bloque carga
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@SuppressWarnings("null")
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		// Crea la etiqueta NBT que se enviará al cliente
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@SuppressWarnings("null")
	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("GridStack", 10)) {
			this.gridStack = ItemStack.parseOptional(registries, tag.getCompound("GridStack"));
		} else {
			this.gridStack = ItemStack.EMPTY;
		}
	}

	@SuppressWarnings("null")
	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.gridStack.isEmpty()) {
			tag.put("GridStack", this.gridStack.save(registries));
		}
	}

	public ItemStack getGridStack() {
		return gridStack;
	}

	@SuppressWarnings("null")
	public void setGridStack(ItemStack stack) {
		this.gridStack = stack;
		this.setChanged();
		// ESTO ES CLAVE: Avisa al mundo que el bloque ha cambiado para que se redibuje
		if (this.level != null) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@SuppressWarnings("null")
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		// Importante: canPlaceItem afecta a los 5 slots de la tolva
		if (slot >= 0 && slot < 5) {
			if (this.gridStack.isEmpty())
				return super.canPlaceItem(slot, stack);

			// Aquí está perfecto: usas la clase Abstracta
			if (this.gridStack.getItem() instanceof AbstractHopperGridItem gridItem)
				return gridItem.canItemPass(this.gridStack, stack);
		}

		return super.canPlaceItem(slot, stack);
	}
}