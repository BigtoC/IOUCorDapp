package com.template.contracts

import net.corda.core.contracts.Contract
import com.template.states.IOUState
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.Test
import net.corda.core.transactions.LedgerTransaction
import net.corda.testing.node.ledger


class ContractTests {
    private val ledgerServices = MockServices(TestIdentity(CordaX500Name("TestId", "", "CN")))
    private val alice = TestIdentity(CordaX500Name("Alice", "", "CN"))
    private val bob = TestIdentity(CordaX500Name("Bob", "", "CN"))

    private val iouState =  IOUState(alice.party, bob.party, 1)

    @Test
    fun testIOUContractType() {
        assert(IOUContract() is Contract)
    }

    @Test
    fun testContractRequiresZeroInputsInTheTransaction() {
        return
    }



}

