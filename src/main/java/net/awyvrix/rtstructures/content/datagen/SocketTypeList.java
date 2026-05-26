package net.awyvrix.rtstructures.content.datagen;

public enum SocketTypeList {
// Power

    POWER("power"),
    LOW_VOLTAGE_POWER("power"),
    MEDIUM_VOLTAGE_POWER("power"),
    HIGH_VOLTAGE_POWER("power"),
    ULTRA_HIGH_VOLTAGE_POWER("power"),

    DIRECT_CURRENT("power"),
    ALTERNATING_CURRENT("power"),

    KEENETIC_ENERGY("power"),
    STRESS_UNITS("power"),
    ENERGY("power"),
    MAGIC_ENERGY("power"),
    STEAM_POWER("power"),
    NUCLEAR_POWER("power"),
    SOLAR_POWER("power"),
    THERMAL_POWER("power"),
    CRYO_POWER("power"),
    QUANTUM_POWER("power"),
    DARK_ENERGY("power"),
    BIO_ENERGY("power"),
    PLASMA_POWER("power"),


// Fluid

    FLUID("fluid"),
    WATER("fluid"),
    STEAM("fluid"),
    OIL("fluid"),
    GAS("fluid"),
    FUEL("fluid"),
    LAVA("fluid"),
    COOLANT("fluid"),
    CHEMICAL("fluid"),
    TOXIC_FLUID("fluid"),
    ACID("fluid"),
    CRYO_FLUID("fluid"),
    BIO_FLUID("fluid"),
    BLOOD("fluid"),
    SLURRY("fluid"),
    WASTE("fluid"),
    OXYGEN("fluid"),
    HYDROGEN("fluid"),
    NITROGEN("fluid"),


// Item

    ITEM("item"),
    BULK_ITEM("item"),
    MICRO_ITEM("item"),
    RESOURCE("item"),
    ORE("item"),
    INGOT("item"),
    FOOD("item"),
    AMMO("item"),
    PACKAGE("item"),
    CONTAINER("item"),
    INVENTORY("item"),
    STORAGE("item"),
    LOGISTICS("item"),
    CARGO("item"),
    MAIL("item"),


// Signals / Data

    SIGNAL("signal"),
    REDSTONE_SIGNAL("signal"),
    DIGITAL_SIGNAL("signal"),
    ANALOG_SIGNAL("signal"),
    DATA("signal"),
    NETWORK_DATA("signal"),
    OPTICAL_DATA("signal"),
    QUANTUM_DATA("signal"),
    WIRELESS_SIGNAL("signal"),
    RADIO_SIGNAL("signal"),
    SATELLITE_SIGNAL("signal"),
    SENSOR("signal"),
    CONTROL("signal"),


// Transport

    ROAD("transport"),
    RAIL("transport"),
    PIPELINE("transport"),
    CONVEYOR("transport"),
    TUNNEL("transport"),
    BRIDGE("transport"),
    FLIGHT_PATH("transport"),
    SHIPPING_ROUTE("transport"),


// Organic

    ROOT("organic"),
    VINE("organic"),
    MYCELIUM("organic"),
    NERVE("organic"),
    TENDON("organic"),
    VEIN("organic"),
    ORGANIC_TUBE("organic"),
    HIVE_CONNECTION("organic"),
    WEB("organic"),


// Sci-fi

    LASER("scifi"),
    BEAM("scifi"),
    TELEPORT("scifi"),
    WORMHOLE("scifi"),
    PORTAL("scifi"),
    DIMENSIONAL_LINK("scifi"),
    TEMPORAL_LINK("scifi"),
    PSIONIC_LINK("scifi"),
    HIVEMIND("scifi"),
    NANITE_STREAM("scifi"),


// Defence / Military

    TARGETING("defense"),
    DEFENSE_GRID("defense"),
    SHIELD("defense"),
    TURRET_CONTROL("defense"),
    MISSILE_LINK("defense"),
    DRONE_CONTROL("defense"),


// Social / Economy

    TRADE("social"),
    MARKET("social"),
    DIPLOMACY("social"),
    FACTION("social"),
    TAX("social"),
    BANKING("social"),


// Environmental

    WEATHER("environment"),
    CLIMATE("environment"),
    HEAT("environment"),
    COLD("environment"),
    PRESSURE("environment"),
    RADIATION("environment"),
    POLLUTION("environment"),


// Abstract / Generic

    GENERIC("generic"),
    UNIVERSAL("generic"),
    CUSTOM("generic"),
    UNKNOWN("generic");

    private final String category;

    SocketTypeList(String category) {
        this.category = category;
    }

    public String category() {
        return category;
    }

    public String id() {
        return name().toLowerCase();
    }

    public String displayName() {
        return "socket." + id();
    }

    public String icon() {
        return "rtstructures:textures/gui/socket/" + id() + ".png";
    }
}