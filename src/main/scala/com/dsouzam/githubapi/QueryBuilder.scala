package com.dsouzam.githubapi

import scala.collection.mutable
/**
  * Created by mattd_000 on 2/19/2017.
  */
// B is the type of the QueryBuilder, Q is the type to build
abstract class QueryBuilder[B, Q] {

  protected val query: String
  protected val qualifiers: mutable.Map[String, Qualifier] = mutable.Map()
  protected val parameters: mutable.Map[String, String] = mutable.Map()

  protected def getBuilderInstance: B

  def build: Q

  /**
    * Adds a qualifier to the query. Will overwrite other qualifiers of the same type.
    * @param qual the qualifier
    * @return query with the updated qualifier
    */
  def qualify(qual: Qualifier): B = {
    qualifiers(qual.left) = qual
    getBuilderInstance
  }
  def qualify(key: String, value: String): B = qualify(Qualifier(key, value))
  def exclude(key: String, value: String): B = qualify(Qualifier(key, value, negate = true))

  /**
    * Adds a parameter to the query. Will overwrite other parameters with the same name.
    * @param param the parameter
    * @param value the value to assign the parameter
    * @return query with the updated parameter
    */
  def addParam(param: String, value: String): B = {
    require(param != "q", "Cannot overwrite the search keyword parameter")
    parameters(param) = value
    getBuilderInstance
  }
  def sortBy(sort: String): B = addParam("sort", sort)
  def orderBy(order: String): B = addParam("order", order)
  def getPage(pageNumber: Int): B = addParam("page", pageNumber.toString)
  def perPage(pageSize: Int): B = addParam("per_page", pageSize.toString)

}
