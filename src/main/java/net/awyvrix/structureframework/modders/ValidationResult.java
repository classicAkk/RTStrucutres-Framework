package net.awyvrix.structureframework.modders;

public final class ValidationResult {
    private final int totalBlocks;
    private final int matchedBlocks;

    public ValidationResult(int totalBlocks, int matchedBlocks) {
        this.totalBlocks = totalBlocks;
        this.matchedBlocks = matchedBlocks;
    }

    public int totalBlocks() {
        return totalBlocks;
    }

    public int matchedBlocks() {
        return matchedBlocks;
    }

    public int damagedBlocks() {
        return totalBlocks - matchedBlocks;
    }

    public float completionPercent() {
        if (totalBlocks == 0) {
            return 100f;
        }

        return (matchedBlocks * 100f) / totalBlocks;
    }

    public float damagedPercent() {

        return 100f - completionPercent();
    }

    public boolean isCompleted() {
        return matchedBlocks == totalBlocks;
    }

    public boolean isDamaged() {
        return matchedBlocks != totalBlocks;
    }
}