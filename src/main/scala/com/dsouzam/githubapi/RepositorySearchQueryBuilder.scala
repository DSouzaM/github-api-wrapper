package com.dsouzam.githubapi

/**
  * Created by mattd_000 on 2/19/2017.
  */
class RepositorySearchQueryBuilder(override val query: String) extends QueryBuilder[RepositorySearchQueryBuilder, SearchQuery] {

  override def getBuilderInstance: RepositorySearchQueryBuilder = this
  override def build: SearchQuery = SearchQuery(query, qualifiers.toMap, parameters.toMap)

  /**
    * Specifies where to search for the query.
    * @param scope any combination of name, description, and readme
    * @return the builder object
    */
  def in(scope: String*) = {
    val fields = Seq("name", "description", "readme")
    require(scope.forall(fields.contains(_)))
    qualify("in", scope.mkString(","))
  }

  /**
    * Specifies the size of the repository.
    * See https://help.github.com/articles/searching-repositories/#search-based-on-the-size-of-a-repository
    * @param expr an expression for the size in kilobytes
    * @return the builder object
    */
  def size(expr: String) = qualify("size", expr)

  /**
    * Searches within a user's repositories.
    * @param username the user to search
    * @return the builder object
    */
  def user(username: String) = qualify("user", username)

  /**
    * Specifies the creation date of the repository.
    * See https://help.github.com/articles/searching-repositories/#search-based-on-when-a-repository-was-created-or-last-updated
    * @param expr an expression for the creation date (typically as YYYY-MM-DD)
    * @return the builder object
    */
  def created(expr: String) = qualify("created", expr)

  /**
    * Specifies a push date of the repository.
    * See https://help.github.com/articles/searching-repositories/#search-based-on-when-a-repository-was-created-or-last-updated
    * @param expr an expression for the push date (typically as YYYY-MM-DD)
    * @return the builder object
    */
  def pushed(expr: String) = qualify("pushed", expr)

  /**
    * Specifies the primary language of the repository.
    * @param lang the language
    * @return the builder object
    */
  def language(lang: String) = qualify("language", lang)

  /**
    * Specifies the number of stars of the repository.
    * @param expr an expresion for the stars
    * @return the builder object
    */
  def stars(expr: String) = qualify("stars", expr)
}