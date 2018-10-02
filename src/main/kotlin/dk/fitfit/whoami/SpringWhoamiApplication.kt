package dk.fitfit.whoami

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress

@SpringBootApplication
class SpringWhoamiApplication

fun main(args: Array<String>) {
    runApplication<SpringWhoamiApplication>(*args)
}

@RestController
class WhoamiController {
    private val logger = LoggerFactory.getLogger(WhoamiController::class.java)

    @GetMapping("/")
    fun whoami(): String {
        logger.info("Whoami request");
        return InetAddress.getLocalHost().getHostName()
    }
}
