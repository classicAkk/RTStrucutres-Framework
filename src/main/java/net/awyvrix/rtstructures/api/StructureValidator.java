package net.awyvrix.rtstructures.api;

import net.awyvrix.rtstructures.core.BlockEntry;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class StructureValidator {

    private StructureValidator() {}

    public static ValidationResult validate(Level level, StructureInstance instance) {
        StructureTemplate template = instance.getStructure();
        int matched = 0;

        for (BlockEntry block : template.blocks) {
            BlockPos pos = instance.toWorldPos(block);
            BlockState expected = template.palette.get(block.paletteId());
            BlockState current = level.getBlockState(pos);

            if (current.equals(expected)) {
                matched++;
            }
        }

        int total = template.blocks.length;

        return new ValidationResult(total, matched);
    }
}