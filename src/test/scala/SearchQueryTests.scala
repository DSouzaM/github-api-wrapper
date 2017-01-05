import com.dsouzam.githubapi.SearchQuery
import org.scalatest.FunSuite


class SearchQueryTests extends FunSuite{
  test("noSort") {
    val query = SearchQuery("wrapper", Some("in:name,description"))
    val params = query.toParams
    assert(params.length == 1 && params.head == ("q", "wrapper in:name,description"))
  }
  test("sortNoOrder") {
    val query = SearchQuery("wrapper", None, Some("stars"))
    val params = query.toParams
    assert(params.length == 2 && params.head == ("q", "wrapper") && params(1) == ("sort", "stars"))
  }
  test("sortWithOrder") {
    val query = SearchQuery("wrapper", None, Some("stars"), Some("asc"))
    val params = query.toParams
    assert(params.length == 3 && params.head == ("q", "wrapper") && params(1) == ("sort", "stars") && params(2) == ("order", "asc"))
  }
  test("orderNoSort") {
    val query = SearchQuery("wrapper", None, None, Some("asc"))
    val params = query.toParams
    assert(params.length == 1 && params.head == ("q", "wrapper"))
  }
}
