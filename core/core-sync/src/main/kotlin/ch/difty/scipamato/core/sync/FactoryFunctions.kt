@file:Suppress("LongParameterList")

package ch.difty.scipamato.core.sync

import java.time.LocalDate
import java.time.LocalDateTime

fun newPublicCode(
    code: String?,
    langCode: String?,
    codeClassId: Int?,
    name: String?,
    comment: String?,
    sort: Int?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicCode = PublicCode(
    code, langCode, codeClassId, name, comment, sort, version, created, lastModified, lastSynched
)

fun newPublicCodeClass(
    codeClassId: Int?,
    langCode: String?,
    name: String?,
    description: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicCodeClass = PublicCodeClass(
    codeClassId, langCode, name, description, version, created, lastModified, lastSynched
)

fun newPublicKeyword(
    id: Int?,
    keywordId: Int?,
    langCode: String?,
    name: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
    searchOverride: String?,
): PublicKeyword = PublicKeyword(
    id, keywordId, langCode, name, version, created, lastModified, lastSynched, searchOverride
)

fun newPublicLanguage(
    code: String?,
    lastSynched: LocalDateTime?,
    mainLanguage: Boolean?,
): PublicLanguage = PublicLanguage(
    code, lastSynched, mainLanguage
)

fun newPublicNewsletter(
    id: Int?,
    issue: String?,
    issueDate: LocalDate?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicNewsletter = PublicNewsletter(
    id, issue, issueDate, version, created, lastModified, lastSynched
)

fun newPublicNewsletterTopic(
    id: Int?,
    langCode: String?,
    title: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicNewsletterTopic = PublicNewsletterTopic(
    id, langCode, title, version, created, lastModified, lastSynched
)

fun newPublicNewStudy(
    newsletterId: Int?,
    paperNumber: Long?,
    newsletterTopicId: Int?,
    sort: Int?,
    year: Int?,
    authors: String?,
    headline: String?,
    description: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicNewStudy = PublicNewStudy(
    newsletterId, paperNumber, newsletterTopicId, sort, year, authors, headline, description,
    version, created, lastModified, lastSynched
)

fun newPublicNewStudyTopic(
    newsletterId: Int?,
    newsletterTopicId: Int?,
    sort: Int?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicNewStudyTopic = PublicNewStudyTopic(
    newsletterId, newsletterTopicId, sort, version, created, lastModified, lastSynched
)

fun newPublicNewStudyPageLink(
    langCode: String?,
    sort: Int?,
    title: String?,
    url: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    lastSynched: LocalDateTime?,
): PublicNewStudyPageLink = PublicNewStudyPageLink(
    langCode, sort, title, url, version, created, lastModified, lastSynched
)

fun newPublicPaper(
    id: Long?,
    number: Long?,
    pmId: Int?,
    authors: String?,
    title: String?,
    location: String?,
    publicationYear: Int?,
    goals: String?,
    methods: String?,
    population: String?,
    result: String?,
    comment: String?,
    version: Int?,
    created: LocalDateTime?,
    lastModified: LocalDateTime?,
    codesPopulation: Array<Short>?,
    codesStudyDesign: Array<Short>?,
    codes: Array<String>?,
    lastSynched: LocalDateTime?,
): PublicPaper = PublicPaper(
    id, number, pmId, authors, title, location, publicationYear, goals, methods, population, result,
    comment, version, created, lastModified, codesPopulation, codesStudyDesign, codes, lastSynched
)
