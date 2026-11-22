@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.jobs

import java.time.LocalDateTime

val CREATED: LocalDateTime = LocalDateTime.now().minusDays(2)
val MODIFIED: LocalDateTime = LocalDateTime.now().minusDays(1)
val SYNCHED: LocalDateTime = LocalDateTime.now()
