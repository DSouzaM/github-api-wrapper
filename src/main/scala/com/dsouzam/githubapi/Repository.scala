package com.dsouzam.githubapi

import java.util.Date

case class Repository (name: String, id: Int, description: String, languages: Map[String, Int], createdAt: Date,
                       updatedAt: Date, pushedAt: Date, stars: Int, watchers: Int, hasPages: Boolean, forks: Int,
                       defaultBranch: String)
