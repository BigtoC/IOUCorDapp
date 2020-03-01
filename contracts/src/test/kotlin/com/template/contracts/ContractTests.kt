package com.template.contracts

import com.template.states.IOUState
import net.corda.core.contracts.Contract
import net.corda.core.identity.CordaX500Name
import net.corda.testing.contracts.DummyState
import net.corda.testing.core.DummyCommandData
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test


class ContractTests {
    private val ledgerServices = MockServices(TestIdentity(CordaX500Name("TestId", "", "CN")))
    private val alice = TestIdentity(CordaX500Name("Alice", "", "CN"))
    private val bob = TestIdentity(CordaX500Name("Bob", "", "CN"))

    private val iouState =  IOUState(alice.party, bob.party, 1)

    private val id: String = "com.template.contracts.IOUContract"

    @Test
    fun `test IOU Contract Type`() {
        assert(IOUContract() is Contract)
    }

    @Test
    fun `Contract Requires Zero Inputs In The Transaction`() {
        ledgerServices.ledger {
            transaction {
                // Has an input, will fail.
                input(id, iouState)
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has no input, will verify.
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }
    }

    @Test
    fun `Contract Requires One Outputs In The Transaction`() {
        ledgerServices.ledger {
            transaction {
                // Has two output, will fail.
                output(id, iouState)
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has no input, will verify.
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }

    }

    @Test
    fun `test Contract Requires One Command In The Transaction`() {
        ledgerServices.ledger {
            transaction {
                output(id, iouState)
                // Has two commands, will fail.
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                output(id, iouState)
                // Has one command, will verify.
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }
    }

    @Test
    fun `test Contract Requires The Transactions Output To Be A TokenState`() {
        ledgerServices.ledger {
            transaction {
                // Has wrong output type, will fail.
                output(id, DummyState())
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has correct output type, will verify.
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }
    }

    @Test
    fun `test Contract Requires Transactions Output To Have A Positive Amount`() {
        val zeroIOUAmount = IOUState(alice.party, bob.party, 0)
        val positiveIOUAmount = IOUState(alice.party, bob.party, 1)
        val negativeIOUAmount = IOUState(alice.party, bob.party, -1)

        ledgerServices.ledger {
            transaction {
                // Has zero-amount TokenState, will fail.
                output(id, zeroIOUAmount)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has negative-amount TokenState, will fail.
                output(id, negativeIOUAmount)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has positive-amount TokenState, will fail.
                output(id, positiveIOUAmount)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }
    }

    @Test
    fun `test Contract Requires The Transactions Command To Be An Issue Command`() {
        ledgerServices.ledger {
            transaction {
                output(id, iouState)
                // Has wrong command type, will fail.
                command(listOf(alice.publicKey, bob.publicKey), DummyCommandData)
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Has correct command type, will verify.
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }
    }

    @Test
    fun `test Contract Requires The Issuer To Be A Required Signer In The Transaction`() {
        val iouStateWhereBobIsIssuer = IOUState(bob.party, alice.party, 1)

        ledgerServices.ledger {
            transaction {
                // Issuer is not a required signer, will fail.
                output(id, iouState)
                command(listOf(bob.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Issuer is also not a required signer, will fail.
                output(id, iouStateWhereBobIsIssuer)
                command(listOf(alice.publicKey), IOUContract.Issue())
                fails()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Issuer is a required signer, will verify.
                output(id, iouState)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }

        ledgerServices.ledger {
            transaction {
                // Issuer is also a required signer, will verify.
                output(id, iouStateWhereBobIsIssuer)
                command(listOf(alice.publicKey, bob.publicKey), IOUContract.Issue())
                verifies()
            }
        }

    }




}

