import com.dsouzam.githubapi.APIClient
import org.scalatest.FunSuite

class APIClientTests extends FunSuite {
  val client = new APIClient
  test("getRepos") {
    val repos = client.getRepos("dsouzam")
    assert(repos.exists(
      repo => {
        val nameVal = repo.get("name")
        nameVal match {
          case Some(name) => name == "2048Clone"
        }
      }
    ))
  }
}
