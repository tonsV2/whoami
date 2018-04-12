package dk.fitfit.whoami

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringWhoamiApplication

fun main(args: Array<String>) {
    runApplication<SpringWhoamiApplication>(*args)
}
