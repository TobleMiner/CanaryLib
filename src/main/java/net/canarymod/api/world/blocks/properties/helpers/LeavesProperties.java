package net.canarymod.api.world.blocks.properties.helpers;

import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.properties.BlockBooleanProperty;
import net.canarymod.api.world.blocks.properties.BlockEnumProperty;
import net.visualillusionsent.utils.Verify;

import static net.canarymod.api.world.blocks.BlockType.AcaciaLeaves;
import static net.canarymod.api.world.blocks.BlockType.OakLeaves;

/**
 * Leaves properties helper
 *
 * @author Jason Jones (darkdiplomat)
 */
public final class LeavesProperties extends WoodProperties {
    public static final BlockBooleanProperty
            decayable = getInstanceFor(OakLeaves, "decayable"),
            checkDecay = getInstanceFor(OakLeaves, "check_decay");
    public static final BlockEnumProperty
            variantOld = getInstanceFor(OakLeaves, "variant"),
            variantNew = getInstanceFor(AcaciaLeaves, "variant");

    /**
     * Applies whether the {@code Leaves} is decayable or not
     *
     * @param block
     *         the {@link net.canarymod.api.world.blocks.Block} to be modified
     * @param value
     *         the {@code boolean} value to apply
     *
     * @return the Block with adjusted state (NOTE: Original Block object is also modified, using the return is unnecessary)
     *
     * @throws java.lang.NullPointerException
     *         Should {@code block} or {@code value} be null
     * @throws java.lang.IllegalArgumentException
     *         Should an invalid property be applied
     */
    public static Block applyDecayable(Block block, boolean value) {
        return apply(block, decayable, value);
    }

    /**
     * Applies whether the {@code Leaves} should check decay or not
     *
     * @param block
     *         the {@link net.canarymod.api.world.blocks.Block} to be modified
     * @param value
     *         the {@code boolean} value to apply
     *
     * @return the Block with adjusted state (NOTE: Original Block object is also modified, using the return is unnecessary)
     *
     * @throws java.lang.NullPointerException
     *         Should {@code block} or {@code value} be null
     * @throws java.lang.IllegalArgumentException
     *         Should an invalid property be applied
     */
    public static Block applyCheckDecay(Block block, boolean value) {
        return apply(block, checkDecay, value);
    }

    public static Block applyVariant(Block block, Variant value) {
        Verify.notNull(block, "Block block");
        if (block.getType().getMachineName().equals(AcaciaLeaves.getMachineName())) {
            return apply(block, variantNew, value);
        }
        return apply(block, variantOld, value);
    }
}