package net.awyvrix.rtstructures.registries;

import java.util.UUID;

public class RawStructureType extends StructureType {

    public RawStructureType(String path) {
        super(UUID.randomUUID().toString(), StructureProperties.create().path(path));
    }
}