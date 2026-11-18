package org.misis.tp.lab3.defense;

import org.junit.jupiter.api.Test;
import org.misis.tp.lab3.Dice;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvasionHandlerTest {

    @Test
    void evadesAndStopsChain() {
        EvasionHandler handler = new EvasionHandler(0.3);
        DamageHandler next = mock(DamageHandler.class);
        handler.setNext(next);
        Damage damage = new Damage(40);

        Dice mockDice = mock(Dice.class);
        when(mockDice.chance(0.3)).thenReturn(true);

        try (MockedStatic<Dice> mockedStatic = mockStatic(Dice.class)) {
            mockedStatic.when(Dice::getInstance).thenReturn(mockDice);

            handler.handle(damage);

            verify(mockDice, times(1)).chance(0.3);
        }

        assertEquals(0, damage.getAmount());
        assertTrue(damage.isEvaded());
        verifyNoInteractions(next);
    }

    @Test
    void forwardsDamageWhenRollFails() {
        EvasionHandler handler = new EvasionHandler(0.3);
        DamageHandler next = mock(DamageHandler.class);
        handler.setNext(next);
        Damage damage = new Damage(40);

        Dice mockDice = mock(Dice.class);
        when(mockDice.chance(0.3)).thenReturn(false);

        try (MockedStatic<Dice> mockedStatic = mockStatic(Dice.class)) {
            mockedStatic.when(Dice::getInstance).thenReturn(mockDice);

            handler.handle(damage);

            verify(mockDice, times(1)).chance(0.3);
        }

        assertEquals(40, damage.getAmount());
        assertFalse(damage.isEvaded());
        verify(next, times(1)).handle(same(damage));
    }
}
