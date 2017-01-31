package com.dsouzam.githubapi

case class Qualifier(left: String, right: String, negate: Boolean = false) {
  override def toString: String = (if (negate) "-" else "") + left + ":\"" + right + "\""
}

/** Consult https://developer.github.com/v3/search/ and https://help.github.com/articles/search-syntax/ for details on
  * parameters and qualifiers.
  */
case class SearchQuery(query: String, qualifiers: Map[String,Qualifier] = Map(), parameters: Map[String,String] = Map()) {

  /**
    * Returns the parameters for the query
    * @return a sequence of key-value pairs
    */
  def toParams: Seq[(String, String)] = {
    ("q", query + qualString) +: parameters.toSeq
  }

  /**
    * Adds a qualifier to the query. Will overwrite other qualifiers of the same type.
    * @param qual the qualifier
    * @return query with the updated qualifier
    */
  def qualify(qual: Qualifier): SearchQuery = copy(qualifiers = qualifiers + (qual.left -> qual))
  def qualify(key: String, value: String): SearchQuery = qualify(Qualifier(key, value))
  def exclude(key: String, value: String): SearchQuery = qualify(Qualifier(key, value, negate = true))

  /**
    * Adds a parameter to the query. Will overwrite other parameters with the same name.
    * @param param the parameter
    * @param value the value to assign the parameter
    * @return query with the updated parameter
    */
  def addParam(param: String, value: String): SearchQuery = {
    assert(param != "q", "Cannot overwrite the search keyword parameter")
    copy(parameters = parameters + (param -> value))
  }
  def sortBy(sort: String): SearchQuery = addParam("sort", sort)
  def orderBy(order: String): SearchQuery = addParam("order", order)
  def getPage(pageNumber: Int): SearchQuery = addParam("page", pageNumber.toString)
  def perPage(pageSize: Int): SearchQuery = addParam("per_page", pageSize.toString)

  /**
    * Concatenates all the qualifiers of the query.
    * The qualifiers are the options included in the "q=_" parameter of the query, and are different from the other
    * parameters (e.g. sort, order)
    * @return the string of concatenated qualifiers
    */
  def qualString: String = {
    qualifiers.foldLeft("")(
      (curr: String, pair: (String, Qualifier)) => {
        val (_, qual) = pair
        curr + " " + qual.toString
      }
    )
  }
}
