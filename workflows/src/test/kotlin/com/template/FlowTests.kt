package com.template

import com.template.flows.*
import com.template.states.IOUState
import net.corda.core.concurrent.CordaFuture
import net.corda.core.contracts.TransactionState
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.internal.requiredContractClassName
import net.corda.core.transactions.SignedTransaction
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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
    fun transactionConstructedByFlowHasOneTokenStateOutputWithTheCorrectAmountAndOwner() {
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
    fun transactionConstructedByFlowHasOneOutputUsingTheCorrectContract() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //


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

    }

    @Test
    @Throws
    fun transactionConstructedByFlowHasOneCommandWithTheIssuerAndTheOwnerAsASigners() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //

    }

    @Test
    @Throws
    fun transactionConstructedByFlowHasNoInputsAttachmentsOrTimeWindows() {
        // ** Init the network ** //
        val flow = IOUFlowInitiator(alice, bob, 99)
        val future: CordaFuture<SignedTransaction> = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.get()

        // ** Run test cases ** //


    }

}








