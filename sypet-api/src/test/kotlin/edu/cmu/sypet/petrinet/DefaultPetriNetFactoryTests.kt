package edu.cmu.sypet.petrinet

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.fail

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultPetriNetFactoryTests {

    @Nested
    inner class AddPlace {

        @Test
        fun `is idempotent`() {
            fail()
        }

        @Test
        fun `the resulting Petri net contains the place`() {
            fail()
        }
    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
            fail()
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            fail()
        }

        @Test
        fun `throws if a type is missing`() {
            fail()
        }
    }

    @Nested
    inner class AddVoidTransition {

        @Test
        fun `is idempotent`() {
            fail()
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            fail()
        }

        @Test
        fun `throws if types are missing`() {
            fail()
        }
    }

    @Nested
    inner class AddCloneTransition {

        @Test
        fun `is idempotent`() {
            fail()
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            fail()
        }

        @Test
        fun `throws if a type is missing`() {
            fail()
        }

        @Test
        fun `throws if types are not the same`() {
            fail()
        }
    }

    @Nested
    inner class AddCastTransition {

        @Test
        fun `is idempotent`() {
            fail()
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            fail()
        }

        @Test
        fun `throws if a type is missing`() {
            fail()
        }

        @Test
        fun `throws if the parameters are not castable`() {
            fail()
        }
    }
}