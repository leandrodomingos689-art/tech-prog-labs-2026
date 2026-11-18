package org.misis.tp.lab3.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WeaponAdapterTest {

    @Test
    void delegatesLogicToAdaptedWeapon() {
        Weapon weapon = mock(Weapon.class);
        when(weapon.getCode()).thenReturn("TEST_SWORD");
        when(weapon.getName()).thenReturn("Тестовый меч");

        assertEquals("TEST_SWORD", weapon.getCode());
        assertEquals("Тестовый меч", weapon.getName());
        verify(weapon, times(1)).getCode();
        verify(weapon, times(1)).getName();
    }
}
