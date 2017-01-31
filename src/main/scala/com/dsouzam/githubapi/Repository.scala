package com.dsouzam.githubapi

import java.util.Date

case class Repository (url: String, name: String, id: Int, description: String, readMe: String, languages: Map[String, Long], createdAt: Date,
                       updatedAt: Date, pushedAt: Date, stars: Int, watchers: Int, hasPages: Boolean, forks: Int,
                       defaultBranch: String)
