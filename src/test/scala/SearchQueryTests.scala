import com.dsouzam.githubapi.{Qualifier, RepositorySearchQueryBuilder, SearchQuery, SearchQueryBuilder}
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
    val builder = new RepositorySearchQueryBuilder("wrapper").user("dsouzam").language("Scala").exclude("size","0")
    val query = builder.build
    val qualifierString = query.getQualifierString
    assert(Seq("user:\"dsouzam\"", "language:\"Scala\"", "-size:\"0\"").forall(qualifierString.contains(_)))
  }
  test("qualifyOverwrite") {
    val builder = new RepositorySearchQueryBuilder("wrapper").user("dsouzam").exclude("user","dsouzam")
    val query = builder.build
    val qualifierString = query.getQualifierString
    assert(qualifierString.trim ==  "-user:\"dsouzam\"")
  }
}
