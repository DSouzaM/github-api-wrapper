package com.dsouzam.githubapi

case class Qualifier(left: String, right: String, negate: Boolean = false) {
  override def toString: String = (if (negate) "-" else "") + left + ":\"" + right + "\""
}

case class SearchQuery (query: String, qualifiers: Map[String,Qualifier] = Map(), sort: Option[String] = None, order: Option[String] = None) {

  /**
    * Returns the parameters for the query
    * @return a sequence of key-value pairs
    */
  def toParams: Seq[(String, String)] = {

    val queryParam = ("q", query + qualString)
    val sortOptions = sort match {
      case Some(s) => Seq(("sort", s)) ++ (order match {
        case Some(o) => Seq(("order", o))
        case None => Nil
      })
      case None => Nil
    }
    queryParam +: sortOptions
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
