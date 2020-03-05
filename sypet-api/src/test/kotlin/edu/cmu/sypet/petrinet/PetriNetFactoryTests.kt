package edu.cmu.sypet.petrinet

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetriNetFactoryTests {

    @Nested
    inner class AddPlace {

        @Test
        fun `is idempotent`() {
        }

        @Test
        fun `the resulting Petri net contains the place`() {
        }
    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
        }

        @Test
        fun `throws if a type is missing`() {
        }
    }

    @Nested
    inner class AddVoidTransition {

        @Test
        fun `is idempotent`() {
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
        }

        @Test
        fun `throws if types are missing`() {
        }
    }

    @Nested
    inner class AddCloneTransition {

        @Test
        fun `is idempotent`() {
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
        }

        @Test
        fun `throws if a type is missing`() {
        }

        @Test
        fun `throws if types are not the same`() {
        }
    }

    @Nested
    inner class AddCastTransition {

        @Test
        fun `is idempotent`() {
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
        }

        @Test
        fun `throws if a type is missing`() {
        }

        @Test
        fun `throws if the parameters are not castable`() {
        }
    }
}