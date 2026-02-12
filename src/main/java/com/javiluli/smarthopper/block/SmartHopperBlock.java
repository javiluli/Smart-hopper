package com.javiluli.smarthopper.block;

import org.jetbrains.annotations.Nullable;

import com.javiluli.smarthopper.item.base.AbstractHopperGridItem;
import com.javiluli.smarthopper.register.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class SmartHopperBlock extends HopperBlock {

	@SuppressWarnings("null")
	public SmartHopperBlock(Properties properties) {
		super(properties.requiresCorrectToolForDrops());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(ENABLED, true));
	}

	@SuppressWarnings("null")
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED);
	}

	@SuppressWarnings("null")
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof SmartHopperBlockEntity smartHopper) {
			// RETIRAR (Shift + Click)
			if (player.isShiftKeyDown() && !smartHopper.getGridStack().isEmpty()) {
				if (!level.isClientSide) {
					ItemStack gridToReturn = smartHopper.getGridStack().copy();
					if (!player.getInventory().add(gridToReturn)) {
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), gridToReturn);
					}
					smartHopper.setGridStack(ItemStack.EMPTY);
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide);
			}

			// INSTALAR (Click con Rejilla)
			if (!player.isShiftKeyDown() && stack.getItem() instanceof AbstractHopperGridItem
					&& smartHopper.getGridStack().isEmpty()) {
				if (!level.isClientSide) {
					smartHopper.setGridStack(stack.copyWithCount(1));
					if (!player.getAbilities().instabuild)
						stack.shrink(1);
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@SuppressWarnings("null")
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		// Si el bloque se destruye (no si solo cambia de estado como
		// activarse/desactivarse)
		if (!state.is(newState.getBlock())) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof SmartHopperBlockEntity smartHopper) {
				// Al picar el bloque, TODO cae al suelo
				if (!smartHopper.getGridStack().isEmpty()) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), smartHopper.getGridStack());
				}
				Containers.dropContents(level, pos, smartHopper);
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@SuppressWarnings("null")
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SmartHopperBlockEntity(pos, state);
	}

	@SuppressWarnings("null")
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		// Usamos el ticker personalizado que definiste para procesar el filtrado
		return createTickerHelper(type, ModBlockEntities.SMART_HOPPER_BE.get(), (lvl, pos, st, be) -> {
			if (be instanceof SmartHopperBlockEntity smartHopper) {
				SmartHopperBlockEntity.pushItemsTick(lvl, pos, st, smartHopper);
			}
		});
	}
}