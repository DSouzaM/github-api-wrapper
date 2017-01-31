import com.dsouzam.githubapi.{Qualifier, SearchQuery}
import org.scalatest.FunSuite


class SearchQueryTests extends FunSuite{
  test("noSort") {
    val query = SearchQuery("wrapper", Map(("in",Qualifier("in", "name,description"))))
    val params = query.toParams
    assert(params.length == 1 && params.head == ("q", "wrapper in:\"name,description\""))
  }
  test("sortNoOrder") {
    val query = SearchQuery("wrapper", Map(), Map("sort" -> "stars"))
    val params = query.toParams
    assert(params.length == 2 && params.head == ("q", "wrapper") && params(1) == ("sort", "stars"))
  }
  test("sortWithOrder") {
    val query = SearchQuery("wrapper", Map(), Map("sort" -> "stars", "order" -> "asc"))
    val params = query.toParams
    assert(params.length == 3 && params.head == ("q", "wrapper") && params(1) == ("sort", "stars") && params(2) == ("order", "asc"))
  }
  test("qualify") {
    val query = SearchQuery("wrapper").qualify("user", "dsouzam").qualify("language","Scala").exclude("size","0")
    assert(query.qualString == " user:\"dsouzam\" language:\"Scala\" -size:\"0\"")
  }
  test("qualifyOverwrite") {
    val query = SearchQuery("wrapper").qualify("user", "dsouzam").exclude("user", "dsouzam")
    assert(query.qualString == " -user:\"dsouzam\"")
  }
}
