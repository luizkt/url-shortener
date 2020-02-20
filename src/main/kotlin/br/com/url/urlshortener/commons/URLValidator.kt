package br.com.url.urlshortener.commons

import java.util.regex.Matcher
import java.util.regex.Pattern

class URLValidator private constructor() {

    fun validateURL(url: String?): Boolean {
        val m: Matcher = URL_PATTERN.matcher(url)
        return m.matches()
    }

    companion object {
        val INSTANCE = URLValidator()
        private const val URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"
        private val URL_PATTERN: Pattern = Pattern.compile(URL_REGEX)
    }
}