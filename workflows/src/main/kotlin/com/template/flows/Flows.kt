package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.IOUContract
import com.template.states.IOUState
import net.corda.core.contracts.Command
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.security.PublicKey

// Video tutorial: https://youtu.be/Mldg_GgbmTE?t=61

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class IOUFlowInitiator(private val issuer: Party,
                       private val owner: Party,
                       private val iouAmount: Int
                ) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        // Initiator flow logic goes here.
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        // 2. 创建要发行的债券IOUState
        val outputState = IOUState(issuer, owner, iouAmount)

        // 3. 把IOUState加上跟IOUContract的参考值 + 4. 利用IOUState的Issuer作为必要的签名者
        val command = Command(IOUContract.Issue(), outputState.getIssuer.owningKey)

        // 1. 创建TransactionBuilder + 5. 把Issuer的Command加到TransactionBuilder
        val txBuilder = TransactionBuilder(notary=notary)
                .addOutputState(outputState)
                .addCommand(command)

        // We sign the transaction.
        val signedTx = serviceHub.signInitialTransaction(txBuilder)

        // Creating a session with the owner.
        val ownerPartySession = initiateFlow(owner)

        // Obtaining the counter party's signature.
        val fullySignedTx = subFlow(CollectSignaturesFlow(
                signedTx,
                listOf(ownerPartySession),
                CollectSignaturesFlow.tracker())
        )

        // We finalise the transaction.
        return subFlow(FinalityFlow(fullySignedTx))
    }
}

@InitiatedBy(IOUFlowInitiator::class)
class IOUFlowResponder(private val counterPartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Responder flow logic goes here.
        val signTransactionFlow = object : SignTransactionFlow(counterPartySession, SignTransactionFlow.tracker()) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
                val output = stx.tx.outputs.single().data
                "This must be an IOU transaction." using (output is IOUState)
            }
        }
        subFlow(signTransactionFlow)
    }
}
