package com.dsouzam.githubapi

import java.text.SimpleDateFormat

import scala.io.Source
import scala.util.Try
import scala.util.parsing.json._
import scalaj.http._


class APIClient(private val authToken: Option[String]) {
  val baseUrl = "https://api.github.com"
  def this() = this(None)

  /**
    * Queries the GitHub API at the specified endpoint. Provides OAuth token, if available.
    * @param target the URL to access
    * @return result returned by request
    */
  private def query(target: String, params: Seq[(String, String)] = Seq(), headers: Seq[(String, String)] = Seq(), acceptFailure: Boolean = false): String = {
    // it may be worth checking that X-RateLimit-Remaining is non-zero.
    // may want to switch http client to an asynchronous one.
    val url = baseUrl + target
    val authParams: Seq[(String, String)] = authToken match {
      case Some(token) => ("access_token", token) +: params
      case None => params
    }
    val response = Http(url).params(authParams).headers(headers).asString
    if (!response.isSuccess) {
      if (acceptFailure) {
        ""
      } else {
        val result = JSON.parseFull(response.body).get.asInstanceOf[Map[String, Any]]
        sys.error(s"Error code ${response.code} when querying $url: ${result("message")}")
      }
    }
    else {
      response.body
    }
  }

  /**
    * Parses a JSON string to either a List[Any] or Map[String, Any], boxing it in an Either.
    * @param json string of JSON
    * @return a List or a Map
    */
  private def parse(json: String): Either[List[Any], Map[String, Any]] = {
    JSON.parseFull(json) match {
      case Some(parsed) => parsed match {
        case list: List[_] => Left(list)
        case map: Map[_,_] => Right(map.asInstanceOf[Map[String, Any]])
      }
      case None => sys.error(s"Unable to parse JSON.\n$json")
    }
  }

  /** Takes a repository as a Map and returns a Repository
    * @param repo Map
    * @return Repository of corresponding Map, if available
    */
  private def generateRepo(repo: Any, withLanguages: Boolean, withReadMe: Boolean): Repository = {
    repo match {
      case result: Map[_,_] =>
        val stringMap = result.asInstanceOf[Map[String, Any]]
        val fullName = stringMap("full_name").asInstanceOf[String]
        val languages: Map[String, Long] = if (withLanguages) getLanguages(fullName) else Map[String, Long]()
        val readMe: String = if (withReadMe) getReadMe(fullName) else ""
        RepositoryResult(stringMap, readMe, languages).toRepository
    }
  }
  /**
    * Takes a sequence of repositories as Lists and returns a sequence of Repositories
    * Makes an API call to retrieve the language information for each repository.
    * @param repos the user to look up
    * @return a sequence of repository maps
    */
  private def generateRepos(repos: List[Any], withLanguages: Boolean, withReadMe: Boolean): Seq[Repository] = repos.map(generateRepo(_,withLanguages,withReadMe))

  def getRepo(user: String, repo: String, withLanguages: Boolean = false, withReadMe: Boolean = false): Repository = {
    val json = query(s"/repos/$user/$repo")
    val parsed = parse(json).right.get
    generateRepo(parsed, withLanguages, withReadMe)
  }
  /**
    * Requests repository information for a user and generates a sequence of Repository objects.
    * @param user the user to look up
    * @return a sequence of Repository objects
    */
  def getRepos(user: String, withLanguages: Boolean = false, withReadMe: Boolean = false): Seq[Repository] = {
    val json = query(s"/users/$user/repos")
    val parsed = parse(json).left.get
    generateRepos(parsed, withLanguages, withReadMe)
  }

  /**
    * Requests language information for a given repository (specified by a full name, e.g. "dsouzam/github-api-wrapper".
    * @param repo the full name of a repository
    * @return a map from languages to bytes of code
    */
  def getLanguages(repo: String): Map[String, Long] = {
    val json = query(s"/repos/$repo/languages")
    val parsed = parse(json).right.get
    parsed.transform((str:String, dbl:Any) => dbl.asInstanceOf[Double].toLong)
  }

  def getReadMe(repo: String): String = {
    query(s"/repos/$repo/readme", headers = Seq("Accept" -> "application/vnd.github.VERSION.raw"), acceptFailure = true)
  }

  /**
    * Requests information for a user and generates a User object.
    * @param user the user to look up
    * @return a User object
    */
  def getUser(user: String): User = {
    val json = query(s"/users/$user")
    val result = UserResult(parse(json).right.get)
    result.toUser
  }

  /**
    * Performs a repository search and returns the sequence of repositories retrieved.
    * @param searchQuery the query
    * @return a sequence of Repositories received in the query result
    */
  def searchRepos(searchQuery: SearchQuery, withLanguages: Boolean = false, withReadMe: Boolean = false): Seq[Repository] = {
    val json = query("/search/repositories", searchQuery.toParams)
    val result = parse(json).right.get
    val repoList = result("items").asInstanceOf[List[Any]]
    generateRepos(repoList, withLanguages, withReadMe)
  }
  def searchRepos(searchQuery: String): Seq[Repository] = searchRepos(SearchQuery(searchQuery))
}

object APIClient {
  val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  /**
    * Attempts to read the first line of a text file "res/token.txt" for an OAuth access token.
    * This token can be generated from your GitHub account under the developer settings.
    * Using a token increases your rate limit from 60/hr to 5000/hr.
    * @return an optional String with the token value
    */
  def getToken: Option[String] = Try(Some(Source.fromFile("res/token.txt").getLines().next())).getOrElse(None)
}
