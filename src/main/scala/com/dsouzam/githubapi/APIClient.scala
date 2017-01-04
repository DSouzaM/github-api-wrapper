package com.dsouzam.githubapi

import scala.util.parsing.json._
import scalaj.http._

class APIClient {

  val baseUrl = "https://api.github.com/"

  def getRepos(user: String): Seq[Map[String, Any]] = {
    val result = Http(s"${baseUrl}users/$user/repos").asString

    val parsed = JSON.parseFull(result.body) match {
      case Some(data) => data match {
        case list: List[_] => list
        case _ => sys.error("Result is not a list.")
      }
      case None => sys.error("Invalid JSON received.")
    }

    parsed.collect{ case repo: Map[_,_] => repo.asInstanceOf[Map[String,Any]] }
  }

  //val target = "users/dsouzam/repos"
  //val url = baseUrl + target
  //val req = Http(url).asString

  /*val parsed = JSON.parseFull(req.body) match {
    case Some(data) => data match {
      case list: List[_] => list
      case _ => sys.error("Result is not a list.")
    }
    case None => sys.error("Invalid JSON received.")
  }

  val repos = parsed.collect{ case repo: Map[_,_] => repo.asInstanceOf[Map[String,Any]] }
  */
}

object APIClient {
  def main(args: Array[String]) {
    println("this works")
  }
}
