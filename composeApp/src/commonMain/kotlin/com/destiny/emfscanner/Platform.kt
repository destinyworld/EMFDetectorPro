package com.destiny.emfscanner

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform