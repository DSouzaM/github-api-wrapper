package com.dsouzam.githubapi

import java.util.Date

class APIResult(result: Map[String, Any]) {
  def getString(key: String): String = result(key).asInstanceOf[String]
  def getInt(key: String): Int = result(key).asInstanceOf[Double].toInt
  def getBoolean(key: String): Boolean = result(key).asInstanceOf[Boolean]
  def getDate(key: String): Date = APIClient.dateFormatter.parse(getString(key))
}

case class RepositoryResult(result: Map[String, Any], readMe: String, languages: Map[String, Long]) extends APIResult(result) {
  def toRepository: Repository = {
    val url = getString("url")
    val name = getString("name")
    val id = getInt("id")
    val description = getString("description")
    val createdAt = getDate("created_at")
    val updatedAt = getDate("updated_at")
    val pushedAt = getDate("pushed_at")
    val stars = getInt("stargazers_count")
    val watchers = getInt("watchers_count")
    val hasPages = getBoolean("has_pages")
    val forks = getInt("forks_count")
    val defaultBranch = getString("default_branch")
    Repository(url, name, id, description, readMe, languages, createdAt, updatedAt, pushedAt, stars, watchers, hasPages, forks, defaultBranch)
  }
}

case class UserResult(result: Map[String, Any]) extends APIResult(result) {
  def toUser: User = {
    val login = getString("login")
    val id = getInt("id")
    val name = getString("name")
    val blog = getString("blog")
    val location = getString("location")
    val email = getString("email")
    val publicRepos = getInt("public_repos")
    val publicGists = getInt("public_gists")
    val followers = getInt("followers")
    val following = getInt("following")
    val createdAt = getDate("created_at")
    val updatedAt = getDate("updated_at")
    User(login, id, name, blog, location, email, publicRepos, publicGists, followers, following, createdAt, updatedAt)
  }
}