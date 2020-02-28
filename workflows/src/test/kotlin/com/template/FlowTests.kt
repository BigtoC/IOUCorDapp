package com.template

import com.template.flows.*
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTests {
    private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
            TestCordapp.findCordapp("com.template.contracts"),
            TestCordapp.findCordapp("com.template.flows")
    )))
    private val nodeA = network.createNode()
    private val nodeB = network.createNode()
    private val alice: Party = nodeA.info.identityFromX500Name(CordaX500Name("Alice", "", "CN"))
    private val bob: Party = nodeA.info.identityFromX500Name(CordaX500Name("Bob", "", "CN"))

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
    fun transactionConstructedByFlowUsesTheCorrectNotary() {
        val flow = IOUFlowInitiator(alice, bob, 1)

    }
}