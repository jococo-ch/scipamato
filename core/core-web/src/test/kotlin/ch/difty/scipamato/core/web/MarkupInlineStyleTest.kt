package ch.difty.scipamato.core.web

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import java.io.File

private val markupFiles: List<File> =
    listOf("src/main/java", "src/main/resources")
        .map(::File)
        .filter(File::isDirectory)
        .flatMap { dir -> dir.walk().filter { it.isFile && it.extension == "html" }.toList() }

private fun findingsOf(pattern: Regex): List<String> = markupFiles.flatMap { file ->
    file.readLines().mapIndexedNotNull { index, line ->
        if (pattern.containsMatchIn(line)) "${file.name}:${index + 1}: ${line.trim()}" else null
    }
}

/**
 * A strict Content Security Policy blocks style attributes and style elements
 * in the markup; the nonce Wicket adds covers its own header items only.
 */
internal class MarkupInlineStyleTest : FunSpec({

    test("the core markup files are found") {
        markupFiles.shouldNotBeEmpty()
    }

    test("the core markup carries no inline style attribute") {
        findingsOf(Regex("""\sstyle\s*=""")).shouldBeEmpty()
    }

    test("the core markup carries no style element") {
        findingsOf(Regex("""<style[\s>]""")).shouldBeEmpty()
    }
})
