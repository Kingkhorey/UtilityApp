package au.edu.jcu.assessment.utilityapp.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Test

class BackdropTest {

    @Test
    fun earlyMorningIsDawn() {
        assertEquals(Backdrop.Dawn, Backdrop.forHour(5))
        assertEquals(Backdrop.Dawn, Backdrop.forHour(8))
    }

    @Test
    fun daytimeIsDay() {
        assertEquals(Backdrop.Day, Backdrop.forHour(9))
        assertEquals(Backdrop.Day, Backdrop.forHour(16))
    }

    @Test
    fun earlyEveningIsDusk() {
        assertEquals(Backdrop.Dusk, Backdrop.forHour(17))
        assertEquals(Backdrop.Dusk, Backdrop.forHour(19))
    }

    @Test
    fun lateEveningAndOvernightAreNight() {
        assertEquals(Backdrop.Night, Backdrop.forHour(20))
        assertEquals(Backdrop.Night, Backdrop.forHour(0))
        assertEquals(Backdrop.Night, Backdrop.forHour(4))
    }
}
