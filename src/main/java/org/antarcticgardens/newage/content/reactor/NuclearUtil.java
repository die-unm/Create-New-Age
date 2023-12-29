package org.antarcticgardens.newage.content.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.antarcticgardens.newage.CreateNewAge;
import org.antarcticgardens.newage.tools.RaycastUtil;

import java.util.List;

public class NuclearUtil {
    public static final TagKey<Block> STOPS_RADIATION = BlockTags.create(new ResourceLocation(CreateNewAge.MOD_ID, "stops_radiation"));
    public static final TagKey<Item> HAZMAT_SUIT = ItemTags.create(new ResourceLocation(CreateNewAge.MOD_ID, "nuclear/hazmat_suit"));

    public static void createRadiation(int length, Level world, BlockPos pos) {
        try { // weird and temporary solution for certain incompatability. TODO
            if (world.isClientSide())
                return;

        List<LivingEntity> entities = world.getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(pos).inflate(length),
                livingEntity -> !isResistant(livingEntity));

        for (LivingEntity le : entities) {
            for (Direction dir : Direction.values()) {
                if (world.getBlockState(pos.relative(dir)).is(STOPS_RADIATION))
                    continue;

                Vec3 start = Vec3.atCenterOf(pos).relative(dir, 0.5f);
                double distance = le.getEyePosition().distanceTo(start);

                if (distance > length)
                    continue;

                Vec3 direction = le.getEyePosition().subtract(start).normalize();
                HitResult hitResult = RaycastUtil.pickFilteredBlockFromPos(world, start, direction, (float) Math.ceil(distance), bs -> bs.is(STOPS_RADIATION));

                if (hitResult instanceof BlockHitResult bhr) {
                    if (world.getBlockState(bhr.getBlockPos()).is(STOPS_RADIATION))
                        continue;

                    if (bhr.getLocation().distanceTo(start) < distance)
                        continue;
                }

                irradiate(le);
                break;
            }
        }
        } catch (Exception ignored) {

        }
    }

    private static boolean isResistant(Entity entity) {
        if (entity instanceof Player pl && pl.isCreative())
            return true;

        for (ItemStack piece : entity.getArmorSlots()) {
            if (!piece.is(HAZMAT_SUIT))
                return false;
        }

        return true;
    }

    private static void irradiate(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 300, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
    }
}
