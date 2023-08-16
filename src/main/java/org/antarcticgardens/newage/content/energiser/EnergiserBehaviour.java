package org.antarcticgardens.newage.content.energiser;

import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.antarcticgardens.newage.CreateNewAge;
import org.joml.Math;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnergiserBehaviour extends BeltProcessingBehaviour {

    public long maxAbsorptionSpeed;
    public EnergiserBlockEntity be;
    public EnergiserBehaviour(EnergiserBlockEntity be) {
        super(be);
        whenItemEnters(this::itemEnter);
        whileItemHeld(this::itemHeld);
        this.be = be;
    }

    public EnergisingRecipe getRecipe(ItemStack stack) {
        if (be.getLevel() == null) {
            return null;
        }

        Optional<EnergisingRecipe> assemblyRecipe =
                SequencedAssemblyRecipe.getRecipe(getWorld(), stack, CreateNewAge.ENERGISING_RECIPE_TYPE.getType(), EnergisingRecipe.class);

        if (assemblyRecipe.isPresent()) {
            return assemblyRecipe.get();
        }

        List<EnergisingRecipe> recipes = be.getLevel().getRecipeManager().getAllRecipesFor(CreateNewAge.ENERGISING_RECIPE_TYPE.getType());

        for (EnergisingRecipe recipe : recipes) {
            if (recipe.test(stack)) {
                return recipe;
            }
        }
        return null;
    
    }

    public EnergisingRecipe currentRecipe;
    public long charged;
    public long needed;

    private boolean shouldCreateParticles = false;

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        charged = nbt.getLong("charged");
        needed = nbt.getLong("needed");
        shouldCreateParticles = nbt.getBoolean("shouldCreateParticles");
        super.read(nbt, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.putLong("charged", charged);
        nbt.putLong("needed", needed);
        nbt.putBoolean("shouldCreateParticles",shouldCreateParticles);
        if (clientPacket)
            shouldCreateParticles = false;
        super.write(nbt, clientPacket);
    }

    public long sinceUpdate = 0;

    @Override
    public void tick() {
        super.tick();
        if (be.getLevel() == null) {
            return;
        }
        if (!be.getLevel().isClientSide()) {
            sinceUpdate--;
            if (sinceUpdate <= 0) {
                needed = 0;
            }
        }

        be.lastCharged = -1;

        if (needed > 0) {
            be.lastCharged =  be.energy.internalExtract(
                    (long) Math.min(maxAbsorptionSpeed * (long) Math.abs(be.getSpeed() * 0.1),
                            needed - charged), false);
            charged += be.lastCharged;
        }

        if (be.getLevel().isClientSide()) {
            be.size -= 0.15f;
            be.size = Math.clamp(0, 1, be.size);
            if (needed > 0 && charged > 0) {
                be.size = (float) Math.lerp(be.size, (float) charged / needed, 0.6);
            } else if(shouldCreateParticles) {
                shouldCreateParticles = false;
                var rand = be.getLevel().getRandom();
                for (int i = 0 ; i < 6 ; i++) {
                    be.getLevel().addParticle(ParticleTypes.GLOW,
                            false,
                            be.getBlockPos().getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.4,
                            be.getBlockPos().getY() - 1.4 + (rand.nextFloat() - 0.5) * 0.4,
                            be.getBlockPos().getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.4,
                            0.0, 0.5, 0.0);

                }
                be.getLevel().playLocalSound(be.getBlockPos().getX() + 0.5,
                        be.getBlockPos().getY() - 1.4,
                        be.getBlockPos().getZ() + 0.5,
                        SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.3f, 2.0f, true);
            }
        }
    }

    private ProcessingResult itemHeld(TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour handler) {
        if (currentRecipe == null) {
            currentRecipe = getRecipe(transportedItemStack.stack);
        }
        if (be.getSpeed() == 0 || currentRecipe == null || be.getLevel() == null) {
            return ProcessingResult.PASS;
        }
        int count = transportedItemStack.stack.getCount();
        needed = (long) count * currentRecipe.energyNeeded;
        sinceUpdate = 10;

        if (charged >= needed) {
            List<TransportedItemStack> out = currentRecipe.rollResults().stream()
                    .map(stack -> {
                        TransportedItemStack copy = transportedItemStack.copy();
                        boolean centered = BeltHelper.isItemUpright(stack);
                        stack.setCount(stack.getCount() * count);
                        copy.stack = stack;
                        copy.angle = centered ? 180 : be.getLevel().getRandom().nextInt(360);
                        return copy;
                    })
                    .collect(Collectors.toList());

            charged = 0;
            needed = 0;

            if (out.isEmpty()) {
                handler.handleProcessingOnItem(transportedItemStack,
                        TransportedItemStackHandlerBehaviour.TransportedResult.removeItem());
            } else {
                handler.handleProcessingOnItem(transportedItemStack,
                        TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(out));
            }
            shouldCreateParticles = true;
            blockEntity.sendData();
            return ProcessingResult.PASS;
        }


        return ProcessingResult.HOLD;
    }

    private ProcessingResult itemEnter(TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour transportedItemStackHandlerBehaviour) {
        if (be.getSpeed() == 0) {
            return ProcessingResult.PASS;
        }

        currentRecipe = getRecipe(transportedItemStack.stack);
        if (currentRecipe == null || currentRecipe.getIngredients().size() > 1)
            return ProcessingResult.PASS;

        return ProcessingResult.HOLD;
    }

}
