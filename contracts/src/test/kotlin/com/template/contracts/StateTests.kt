package com.template.contracts

import net.corda.testing.node.MockServices
import org.junit.Test
import com.template.states.IOUState
import net.corda.core.contracts.ContractState
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.testing.core.TestIdentity
import org.jgroups.util.Util.assertTrue
import kotlin.test.assertEquals

class StateTests {
    private val ledgerServices = MockServices()
    private val alice: Party = TestIdentity(CordaX500Name("Alice", "", "CN")).party
    private val bob: Party = TestIdentity(CordaX500Name("Bob", "", "CN")).party

    @Test
    fun testIOUStateGetAttrs() {
        val iouState = IOUState(alice, bob, 1)
        assertEquals(alice, iouState.getIssuer, "Same issuer")
        assertEquals(bob, iouState.getOwner, "Same owner")
        assertEquals(1, iouState.getAmount, "Same amount")
    }

    @Test
    fun testStateHasTwoParticipants() {
        val iouState = IOUState(alice, bob, 1)
        val participants = iouState.participants
        assertEquals(2, participants.size)
        assertTrue(participants.contains(alice))
        assertTrue(participants.contains(bob))
    }

    @Test
    fun testIOUStateType() {
        assert(IOUState(alice, bob, 1) is ContractState)
    }


}