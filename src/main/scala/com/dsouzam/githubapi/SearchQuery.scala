package com.dsouzam.githubapi

case class SearchQuery (query: String, qualifiers: Option[String] = None, sort: Option[String] = None, order: Option[String] = None) {
  def toParams: Seq[(String, String)] = {
    val queryParam = ("q", if (qualifiers.isDefined) query + " " + qualifiers.get else query)
    val sortOptions = sort match {
      case Some(s) => Seq(("sort", s)) ++ (order match {
        case Some(o) => Seq(("order", o))
        case None => Nil
      })
      case None => Nil
    }
    queryParam +: sortOptions
  }
}
