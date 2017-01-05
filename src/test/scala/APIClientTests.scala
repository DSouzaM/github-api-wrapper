import com.dsouzam.githubapi.APIClient
import org.scalatest.FunSuite

import java.util.GregorianCalendar


class APIClientTests extends FunSuite {
  val token: Option[String] = APIClient.getToken
  val client = new APIClient(token)

  test("getRepos") {
    val repos = client.getRepos("dsouzam")
    val calendar = new GregorianCalendar
    calendar.set(2017, 0, 4, 20, 2, 48) // the repository for this project was created 2017-01-04 20:02:48
    assert(repos.exists(repo => repo.name == "github-api-wrapper" && APIClient.dateFormatter.format(calendar.getTime) == APIClient.dateFormatter.format(repo.createdAt)))
  }
}