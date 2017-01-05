package com.dsouzam.githubapi

import java.util.Date

case class User (login: String, id: Int, name: String, blog: String, location: String, email: String, publicRepos: Int,
                 publicGists: Int, followers: Int, following: Int, createdAt: Date, updatedAt: Date)