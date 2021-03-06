package com.template.webserver

import com.template.states.IOUState
import net.corda.core.messaging.vaultQueryBy
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.stereotype.Controller
import org.springframework.ui.Model

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
class Controller(rpc: NodeRPCConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    private val proxy = rpc.proxy

    @GetMapping(value = ["/status"], produces = ["text/plain"])
    private fun status() = "200"

    @GetMapping(value = ["/tempEndpoint"], produces = ["text/plain"])
    private fun tempEndpoint(@RequestParam(value = "name")name: String, model: Model): String {
        model.addAttribute("name", name)
        val id = proxy.nodeInfo().legalIdentities.toString()
        model.addAttribute("id", id)
        return "index"
    }

    @GetMapping(value = ["/servertime"], produces = ["text/plain"])
    private fun serverTime() = LocalDateTime.ofInstant(proxy.currentNodeTime(), ZoneId.of("UTC")).toString()

    @GetMapping(value = ["/addresses"], produces = ["text/plain"])
    private fun addresses() = proxy.nodeInfo().addresses.toString()

    @GetMapping(value = ["/identities"], produces = ["text/plain"])
    private fun identities() = proxy.nodeInfo().legalIdentities.toString()

    @GetMapping(value = ["/platformversion"], produces = ["text/plain"])
    private fun platformVersion() = proxy.nodeInfo().platformVersion.toString()

    @GetMapping(value = ["/peers"], produces = ["text/plain"])
    private fun peers() = proxy.networkMapSnapshot().flatMap { it.legalIdentities }.toString()

    @GetMapping(value = ["/notaries"], produces = ["text/plain"])
    private fun notaries() = proxy.notaryIdentities().toString()

    @GetMapping(value = ["/flows"], produces = ["text/plain"])
    private fun flows() = proxy.registeredFlows().toString()

    @GetMapping(value = ["/states"], produces = ["text/plain"])
    private fun states() = proxy.vaultQueryBy<IOUState>().states.toString()
}
