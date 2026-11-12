package org.misis.tp.lab3.item;

public class DefaultCatalog {
    public static final String TRAINING_SWORD = "training-sword";
    public static final String HUNTING_BOW = "hunting-bow";
    public static final String APPRENTICE_STAFF = "apprentice-staff";
    public static final String CHAINMAIL = "chainmail";
    public static final String LEATHER_ARMOR = "leather-armor";
    public static final String LINEN_ROBE = "linen-robe";

    public static final String IRON_SWORD = "iron-sword";
    public static final String ELVEN_BOW = "elven-bow";
    public static final String EMBER_STAFF = "ember-staff";
    public static final String PLATE_ARMOR = "plate-armor";

    public static final String GOBLIN_CLEAVER = "goblin-cleaver";
    public static final String BONE_BOW = "bone-bow";
    public static final String NECROMANCER_STAFF = "necromancer-staff";
    public static final String KNIGHT_BLADE = "knight-blade";
    public static final String CRYPT_MAIL = "crypt-mail";
    public static final String DAWN_BLADE = "dawn-blade";
    public static final String DRAGONSCALE_ARMOR = "dragonscale-armor";

    public static ItemCatalog create() {
        ItemCatalog catalog = new ItemCatalog();
        catalog.register(new Sword(TRAINING_SWORD, "Тренировочный меч", 20, 12));
        catalog.register(new Bow(HUNTING_BOW, "Охотничий лук", 20, 10, 0.3));
        catalog.register(new Staff(APPRENTICE_STAFF, "Посох ученика", 20, 14, 0.3));
        catalog.register(new Armor(CHAINMAIL, "Кольчуга", 40, 0.3));
        catalog.register(new Armor(LEATHER_ARMOR, "Кожаная броня", 30, 0.2));
        catalog.register(new Armor(LINEN_ROBE, "Льняная роба", 15, 0.1));

        catalog.register(new Sword(IRON_SWORD, "Железный меч", 80, 17));
        catalog.register(new Bow(ELVEN_BOW, "Эльфийский лук", 90, 14, 0.4));
        catalog.register(new Staff(EMBER_STAFF, "Тлеющий посох", 90, 20, 0.3));
        catalog.register(new Armor(PLATE_ARMOR, "Латы", 120, 0.4));

        catalog.register(new Sword(GOBLIN_CLEAVER, "Гоблинский тесак", 40, 15));
        catalog.register(new Bow(BONE_BOW, "Костяной лук", 140, 17, 0.4));
        catalog.register(new Staff(NECROMANCER_STAFF, "Посох некроманта", 150, 25, 0.35));
        catalog.register(new Sword(KNIGHT_BLADE, "Клинок рыцаря", 150, 23));
        catalog.register(new Armor(CRYPT_MAIL, "Кольчуга стража склепа", 160, 0.45));
        catalog.register(new Sword(DAWN_BLADE, "Клинок рассвета", 500, 35));
        catalog.register(new Armor(DRAGONSCALE_ARMOR, "Драконья чешуя", 500, 0.55));
        return catalog;
    }
}
