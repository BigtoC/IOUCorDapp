package com.template.states

import com.template.contracts.IOUContract
import com.template.contracts.TemplateContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party

// *********
// * State *
// *********
@BelongsToContract(IOUContract::class)
class IOUState(private val issuer: Party,
               private val owner: Party,
               private val amount: Int
               ) : ContractState {
    override val participants get() = listOf(issuer, owner)

    val getIssuer get() = issuer
    val getOwner get() = owner
    val getAmount get() = amount
}
