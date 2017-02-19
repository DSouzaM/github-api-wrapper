package com.dsouzam.githubapi

/**
  * Created by mattd_000 on 2/19/2017.
  */
class SearchQueryBuilder(override val query: String) extends QueryBuilder[SearchQueryBuilder, SearchQuery] {
  override def getBuilderInstance: SearchQueryBuilder = this
  override def build: SearchQuery = SearchQuery(query, qualifiers.toMap, parameters.toMap)
}
