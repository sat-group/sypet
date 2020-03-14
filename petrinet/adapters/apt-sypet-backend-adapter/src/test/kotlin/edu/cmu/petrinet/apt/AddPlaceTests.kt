package edu.cmu.petrinet.apt

import edu.cmu.petrinet.sypet.BackendPlace
import edu.cmu.petrinet.sypet.PlaceAlreadyExistsException
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import uniol.apt.adt.pn.PetriNet

fun newMockPlace(): BackendPlace {
    val mockPlace = mockk<BackendPlace>()

    every { mockPlace.id() } returns "0"

    return mockPlace
}

class AddPlaceTests : BehaviorSpec({
    given("an empty Petri net") {
        and("and a null place") {
            `when`("added to the net") {
                then("should throw IllegalArgumentException") {
                    // Arrange.
                    val adapter = SyPetBackendAdapter(PetriNet())
                    val newPlace: BackendPlace? = null

                    // Act.
                    val action = { adapter.add(newPlace) }

                    // Assert.
                    shouldThrow<IllegalArgumentException>(action)
                }
            }
        }

        and("a new place") {
            `when`("the place is added to the net") {
                then("the net should contain the place") {
                    // Arrange.
                    val adapter = SyPetBackendAdapter(PetriNet())
                    val newPlace = newMockPlace()

                    // Act.
                    adapter.add(newPlace)

                    // Assert.
                    adapter.contains(newPlace).shouldBeTrue()
                }
            }

            `when`("the place is added twice to the net") {
                then("should throw PlaceAlreadyExistsException") {
                    // Arrange.
                    val adapter = SyPetBackendAdapter(PetriNet())
                    val newPlace = newMockPlace()

                    // Act.
                    val action = {
                        adapter.add(newPlace)
                        adapter.add(newPlace)
                    }

                    // Assert.
                    shouldThrow<PlaceAlreadyExistsException>(action)
                }
            }
        }
    }
})