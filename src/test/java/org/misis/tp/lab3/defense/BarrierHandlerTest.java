package org.misis.tp.lab3.defense;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BarrierHandlerTest {

    @ParameterizedTest(name = "урон {0}: поглощено {1}, прошло {2}")
    @MethodSource("damageCases")
    void handlesDamageBoundaries(int incoming, int absorbed, int remaining) {
        BarrierHandler barrier = new BarrierHandler(30);
        Damage damage = new Damage(incoming);

        barrier.handle(damage);

        assertEquals(absorbed, damage.getAbsorbed());
        assertEquals(remaining, damage.getAmount());
    }

    static Stream<Arguments> damageCases() {
        return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(10, 10, 0),
                Arguments.of(30, 30, 0),
                Arguments.of(40, 30, 10)
        );
    }

    @Test
    void spendsCapacityAcrossHits() {
        BarrierHandler barrier = new BarrierHandler(30);
        Damage first = new Damage(20);
        Damage second = new Damage(20);
        Damage third = new Damage(20);

        barrier.handle(first);
        barrier.handle(second);
        barrier.handle(third);

        assertEquals(20, first.getAbsorbed());
        assertEquals(0, first.getAmount());
        assertEquals(10, second.getAbsorbed());
        assertEquals(10, second.getAmount());
        assertEquals(0, third.getAbsorbed());
        assertEquals(20, third.getAmount());
    }
}
