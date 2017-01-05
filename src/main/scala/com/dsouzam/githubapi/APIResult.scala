package com.dsouzam.githubapi

import java.util.Date

class APIResult(result: Map[String, Any]) {
  def getString(key: String): String = result(key).asInstanceOf[String]
  def getInt(key: String): Int = result(key).asInstanceOf[Double].toInt
  def getBoolean(key: String): Boolean = result(key).asInstanceOf[Boolean]
  def getDate(key: String): Date = APIClient.dateFormatter.parse(getString(key))
}

case class RepositoryResult(result: Map[String, Any]) extends APIResult(result) {
  def toRepository: Repository = {
    val name = getString("name")
    val id = getInt("id")
    val description = getString("description")
    val languages = new scala.collection.immutable.HashMap[String,Int] // todo
    val createdAt = getDate("created_at")
    val updatedAt = getDate("updated_at")
    val pushedAt = getDate("pushed_at")
    val stars = getInt("stargazers_count")
    val watchers = getInt("watchers_count")
    val hasPages = getBoolean("has_pages")
    val forks = getInt("forks_count")
    val defaultBranch = getString("default_branch")
    Repository(name, id, description, languages, createdAt, updatedAt, pushedAt, stars, watchers, hasPages, forks, defaultBranch)
  }
}
