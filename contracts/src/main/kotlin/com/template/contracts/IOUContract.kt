package com.template.contracts

import com.template.states.IOUState
import net.corda.core.contracts.*
import net.corda.core.identity.Party
import net.corda.core.transactions.LedgerTransaction
import java.security.PublicKey

// ************
// * Contract *
// ************
class IOUContract : Contract {
    companion object {
        // Used to identify our contract when building a transaction.
        const val ID = "com.template.IOUContracts"
    }

    class Issue : CommandData

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    override fun verify(tx: LedgerTransaction) {
        // Verification logic goes here.

        requireThat {
            "3. 一个命令command - Only accept one command" using(tx.commands.size == 1)
        }


        when (tx.getCommand<CommandData>(0).value) {
            is Issue -> {  // 6. 命令command的内容是Issue
                val command = tx.commands.requireSingleCommand<Issue>()

                requireThat {
                    // "Shape" Constraint - no. of input/output state, no. command
                    "1. 没有输入 - Issue transaction must have no inputs" using(tx.inputStates.isEmpty())
                    "2. 一个输出 - Issue transaction must have only one output" using(tx.outputStates.size == 1)

                    // Content Constraint - business(业务) Constraint
                    val outputState: ContractState = tx.outputStates[0]
                    "4. 一个输出是IOUState - The output must be an IOUState" using(outputState is IOUState)
                    val iouOutput = outputState as IOUState
                    "5. 输出金额为正数 - The output amount must be a positive value" using(iouOutput.getAmount > 0)

                    // Required Signer Constraint - 需要哪些人签名
                    val issuer: Party = iouOutput.getIssuer
                    val issuerKey: PublicKey = issuer.owningKey
                    "Issue transaction must have at least one sign" using(command.signers.isNotEmpty())
                    "7. 发行人Issuer是签名者 - Issuer must sign" using(command.signers[0] == issuerKey)

                }
            }
            else -> {
                throw IllegalArgumentException("Unrecognized command")
            }
        }
    }

}
