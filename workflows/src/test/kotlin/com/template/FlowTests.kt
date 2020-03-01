package com.template

import com.template.contracts.IOUContract
import com.template.flows.*
import com.template.states.IOUState
import net.corda.core.concurrent.CordaFuture
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FlowTests {
    private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
            TestCordapp.findCordapp("com.template.contracts"),
            TestCordapp.findCordapp("com.template.flows")
    )))
    private val aName = CordaX500Name("Alice", "", "CN")
    private val bName = CordaX500Name("Bob", "", "CN")
    private val nodeA = network.createNode(aName)
    private val nodeB = network.createNode(bName)
    private val alice: Party = nodeA.info.identityFromX500Name(aName)
    private val bob: Party = nodeB.info.identityFromX500Name(bName)

    init {
        listOf(nodeA, nodeB).forEach {
            it.registerInitiatedFlow(IOUFlowResponder::class.java)
        }
    }

    @Before
    fun setup() = network.runNetwork()

    @After
    fun tearDown() = network.stopNodes()

    @Test
    @Throws
    fun transactionConstructedByFlowWithOnlyOneOutput() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(1, signedTransaction.tx.outputStates.size)
        val output = signedTransaction.tx.outputStates

//        assertEquals(network.notaryNodes[0].info.legalIdentities[0], output[0])
    }

    @Test
    @Throws
    fun `transaction Constructed By Flow Has One Token State Output With The Correct Amount And Owner`() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(1, signedTransaction.tx.outputStates.size)

        val output = signedTransaction.tx.outputsOfType(IOUState::class.java)[0]

        assertEquals(nodeB.info.legalIdentities[0], output.getOwner)
        assertEquals(99, output.getAmount)
    }

    @Test
    @Throws
    fun `transaction Constructed By Flow Has One Output Using The Correct Contract`() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(1, signedTransaction.tx.outputStates.size)
        val output = signedTransaction.tx.outputs[0]
        print(output.contract)
        assertEquals("com.template.contracts.IOUContract", output.contract)

    }

    @Test
    @Throws
    fun transactionConstructedByFlowHasOneIssueCommand() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(1, signedTransaction.tx.outputStates.size)
        val command = signedTransaction.tx.commands[0]

        assert(command.value is IOUContract.Issue)
    }

    @Test
    @Throws
    fun `transaction Constructed By Flow Has One Command With The Issuer And The Owner As A Signer`() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(1, signedTransaction.tx.outputStates.size)
        val command = signedTransaction.tx.commands[0]

        assertEquals(2, command.signers.size)
        assertTrue(command.signers.contains(nodeA.info.legalIdentities[0].owningKey))
        assertTrue(command.signers.contains(nodeB.info.legalIdentities[0].owningKey))

    }

    @Test
    @Throws
    fun `transaction Constructed By Flow Has No Inputs Attachments Or Time Windows`() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //
        assertEquals(0, signedTransaction.tx.inputs.size)
        // The single attachment is the contract attachment.
        assertEquals(1, signedTransaction.tx.attachments.size)
        assertNull(signedTransaction.tx.timeWindow)
    }

}








