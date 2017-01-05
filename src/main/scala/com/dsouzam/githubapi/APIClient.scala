package com.dsouzam.githubapi

import java.text.SimpleDateFormat

import scala.io.Source
import scala.util.Try
import scala.util.parsing.json._
import scalaj.http._


class APIClient(private val authToken: Option[String]) {
  val baseUrl = "https://api.github.com"
  def this() = this(None)

  /***
    * Performs a GET request to the specified URL. Provides OAuth token, if available.
    * @param url the URL to access
    * @return JSON result returned by request
    */
  private def query(url: String): String = {
    // it may be worth checking that X-RateLimit-Remaining is non-zero.
    // may want to switch http client to an asynchronous one.
    val params = authToken match {
      case Some(token) => Seq(("access_token", token))
      case None => Seq()
    }
    val response = Http(url).params(params).asString
    if (!response.isSuccess) {
      val result = JSON.parseFull(response.body).get.asInstanceOf[Map[String, Any]]
      sys.error(s"Error code ${response.code} when querying $url: ${result("message")}")
    }
    else {
      response.body
    }
  }

  /***
    * Performs the API call and parses the JSON result into a Seq of Maps corresponding to each repository.
    * @param user the user to look up
    * @return a sequence of repository maps
    */
  private def requestRepos(user: String): Seq[RepositoryResult] = {
    val json = query(s"$baseUrl/users/$user/repos")

    val parsed = JSON.parseFull(json) match {
      case Some(data) => data match {
        case list: List[_] => list
        case _ => sys.error("Result is not a list.")
      }
      case None => sys.error("Invalid JSON received.")
    }
    parsed.collect{ case result: Map[_,_] => RepositoryResult(result.asInstanceOf[Map[String,Any]]) }
  }

  /***
    * Requests repository information and generates a sequence of Repository objects.
    * @param user the user to look up
    * @return a sequence of Repository objects
    */
  def getRepos(user: String): Seq[Repository] = {
    val repos = requestRepos(user)
    repos.map(_.toRepository)
  }
}

object APIClient {
  val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  /***
    * Attempts to read the first line of a text file "res/token.txt" for an OAuth access token.
    * This token can be generated from your GitHub account under the developer settings.
    * Using a token increases your rate limit from 60/hr to 5000/hr.
    * @return an optional String with the token value
    */
  def getToken: Option[String] = Try(Some(Source.fromFile("res/token.txt").getLines().next())).getOrElse(None)
}
