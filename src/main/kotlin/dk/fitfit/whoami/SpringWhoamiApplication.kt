package dk.fitfit.whoami

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.net.InetAddress
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

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
        logger.info("Whoami request")
        return InetAddress.getLocalHost().hostName
    }
}

@Entity
class Repo(val url: String = "", @Id @GeneratedValue val id: Long = 0)

@Entity
class Stack(
        val name: String = "",
        @OneToMany val repos: List<Repo> = listOf(),
        @Id @GeneratedValue val id: Long = 0
)

@Repository
interface RepoRepository : JpaRepository<Repo, Long>

@Repository
interface StackRepository : JpaRepository<Stack, Long>

interface StackService {
    fun save(stack: Stack): Stack
    fun findAll(): List<Stack>
    fun getOne(id: Long): Stack
    fun getCompose(id: Long): String
    fun getKompose(id: Long): Stack
    fun save(id: Long, repo: Repo): Stack
}

@Service
class StackServiceImpl(val repoRepository: RepoRepository, val stackRepository: StackRepository) : StackService {
    override fun getCompose(id: Long): String {
        val stack = getOne(id)
        // TODO: Call DockerComposeService.compose(repos: List<Repo>)
        val s = stack.repos.joinToString("\n---\n") {
            it.url
        }
        println(s)
        return s
    }

    override fun getKompose(id: Long): Stack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // TODO: Call KomposeService.?

    }

    override fun save(id: Long, repo: Repo): Stack {
        val stack = stackRepository.getOne(id)
        val newRepo = repoRepository.save(repo)
        val newStack = Stack(stack.name, stack.repos + newRepo, stack.id)
        return stackRepository.save(newStack)
    }

    override fun getOne(id: Long): Stack {
        return stackRepository.getOne(id)
    }

    override fun findAll(): List<Stack> {
        return stackRepository.findAll()
    }

    override fun save(stack: Stack): Stack {
        return stackRepository.save(stack)
    }
}

@RestController
@RequestMapping("/stacks")
class StackCotroller(val stackService: StackService) {
    @PostMapping
    fun save(@RequestBody stack: Stack) = stackService.save(stack)

    @PostMapping("/{id}/repositories")
    fun save(@PathVariable id: Long, @RequestBody repo: Repo) = stackService.save(id, repo)

    @GetMapping
    fun findAll() = stackService.findAll()

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long) = stackService.getOne(id)

    @GetMapping("/{id}/compose")
    fun getCompose(@PathVariable id: Long) = stackService.getCompose(id)

    @GetMapping("/{id}/kompose")
    fun getKompose(@PathVariable id: Long) = stackService.getKompose(id)
}

// Cmd util for launching stacks? Well actually just for adding and getting... The actual launching should be done by docker swarm or kubectl
// stackmire list
// stackmire rm name
// stackmire create name
// stackmire info name
// stackmire add name https://git-repo.com
// stackmire compose name
// docker swarm stack -c ...
