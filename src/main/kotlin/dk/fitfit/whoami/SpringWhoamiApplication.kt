package dk.fitfit.whoami

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress
import javax.servlet.http.HttpServletRequest

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

    @GetMapping("/exception")
    fun whoami(request: HttpServletRequest): String {
        logger.info("Whoami exception");
        throw Exception("shalalala except!")
    }
}

// https://github.com/traverson/traverson/blob/master/user-guide.markdown#browser
// https://ethanent.github.io/phin/
/*
val http = {
    constructor(url)
    get, post, delete, etc
}

val api = {
    constructor(url) {
        http.get(url).then(res => {
            enhanceObject(res.data)
        })
    },
    private enhanceObject(res) {
        res.foreach(data => {
            if data._links
            links = parseLinks(data._links)
            self = links.getLink("self")
            http = http(self)
            data.get = (rel) => rel ? http.get(rel) : http.get()
            data.post = (rel) => rel ? http.post(rel) : http.post()
            data.delete = ...
            if data is array | object
                enhanceObject(data)
        })
    },
    private parseLinks(links) {}
}
*/
