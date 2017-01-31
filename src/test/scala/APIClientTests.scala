import com.dsouzam.githubapi.{APIClient, Qualifier, SearchQuery}
import org.scalatest.FunSuite
import java.util.GregorianCalendar


class APIClientTests extends FunSuite {
  val token: Option[String] = APIClient.getToken
  val client = new APIClient(token)

  test("getRepo") {
    val repo = client.getRepo("dsouzam", "github-api-wrapper")
    assert(repo.name == "github-api-wrapper")
  }

  test("getRepoWithLanguageAndReadMe") {
    val repoWithStuff = client.getRepo("dsouzam", "dsouzam.github.io", withLanguages=true, withReadMe=true)
    val repoWithoutStuff = client.getRepo("dsouzam", "dsouzam.github.io")
    assert(repoWithStuff.languages.nonEmpty && repoWithStuff.readMe.nonEmpty)
    assert(repoWithoutStuff.languages.isEmpty && repoWithoutStuff.readMe.isEmpty)
  }

  test("getRepos") {
    val repos = client.getRepos("dsouzam")
    val calendar = new GregorianCalendar
    calendar.set(2017, 0, 4, 20, 2, 48) // the repository for this project was created 2017-01-04 20:02:48
    assert(repos.exists(repo => repo.name == "github-api-wrapper" && APIClient.dateFormatter.format(calendar.getTime) == APIClient.dateFormatter.format(repo.createdAt)))
  }

  test("getUser") {
    val user = client.getUser("dsouzam")
    assert(user.id == 6363768 && user.blog == "mattdsouza.com")
  }

  test("searchRepos") {
    val query = SearchQuery("", Map(("user",Qualifier("user","dsouzam")),("language",Qualifier("language","Scala"))), Map("sort" -> "stars", "order" -> "asc"))
    val repos = client.searchRepos(query)
    assert(repos.exists(repo => repo.name == "github-api-wrapper"))
  }
}
