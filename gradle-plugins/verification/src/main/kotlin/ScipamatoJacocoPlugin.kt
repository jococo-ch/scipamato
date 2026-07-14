import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

@Suppress("unused")
class ScipamatoJacocoPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply<JacocoPlugin>()
            val test = tasks.named("test")
            tasks.withType<JacocoReport> {
                val execFiles = project.objects.fileTree()
                    .from(project.layout.buildDirectory.dir("jacoco"))
                    .apply { include("*.exec") }
                executionData(execFiles)
                sourceSets(project.extensions.getByType(SourceSetContainer::class.java).getByName("main"))
                reports {
                    xml.required.set(true)
                    html.required.set(false)
                }
                dependsOn(test)
            }
            target.rootProject.tasks.named("sonar") {
                dependsOn(tasks.named("check"))
                dependsOn(tasks.named("jacocoTestReport"))
            }
        }
    }
}
