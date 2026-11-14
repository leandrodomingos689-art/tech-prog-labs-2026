package org.misis.tp.lab3.progress;

import org.misis.tp.lab3.core.UnknownCodeError;

public enum Achievement {
    FIRST_BLOOD("first-blood", "Первая кровь", "одержать первую победу"),
    LUCKY("lucky", "Счастливчик", "найти зачарованный лут"),
    VETERAN("veteran", "Ветеран", "достичь 5 уровня"),
    DRAGON_SLAYER("dragon-slayer", "Драконоборец", "победить дракона");

    private final String code;
    private final String displayName;
    private final String description;

    Achievement(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

    public static Achievement byCode(String code) {
        for (Achievement a : values()) {
            if (a.code.equals(code)) return a;
        }
        throw new UnknownCodeError("Достижение", code);
    }
}
