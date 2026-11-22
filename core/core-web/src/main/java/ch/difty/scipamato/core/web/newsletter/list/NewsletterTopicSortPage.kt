package ch.difty.scipamato.core.web.newsletter.list

import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.persistence.NewsletterTopicService
import ch.difty.scipamato.core.web.behavior.SortableBehavior
import ch.difty.scipamato.core.web.common.BasePage
import ch.difty.scipamato.core.web.fixed
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconTypeBuilder
import org.apache.wicket.AttributeModifier
import org.apache.wicket.PageReference
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.spring.injection.annot.SpringBean

@Suppress("SameParameterValue")
class NewsletterTopicSortPage(
    model: IModel<Newsletter>,
    private val previousPageRef: PageReference?
) : BasePage<Newsletter>(model) {

    @SpringBean
    private lateinit var service: NewsletterTopicService

    private lateinit var topics: List<NewsletterNewsletterTopic>

    private val order: IModel<String> = Model.of("")

    override fun onInitialize() {
        super.onInitialize()

        check(modelObject?.id != null) { "Cannot start page w/o non-null newsletter topic id in model" }
        val newsletterTopicId = modelObject!!.id!!
        service.removeObsoleteNewsletterTopicsFromSort(newsletterTopicId)
        topics = service.getSortedNewsletterTopicsForNewsletter(newsletterTopicId)

        queue(newHeader("header"))
        queue(Form<Unit>("form"))
        val orderField = HiddenField("order", order)
        queue(orderField)
        queue(WebMarkupContainer("sortable").add(SortableBehavior(orderField)))
        queue(newListView("items", ListModel(topics)))
        queue(newSubmitButton("submit"))
        queue(newCancelButton("cancel"))
    }

    private fun newHeader(id: String): Label =
        Label(id, StringResourceModel("$id$LABEL", this, this.model))

    private fun newSubmitButton(id: String): BootstrapAjaxButton =
        object : BootstrapAjaxButton(
            id,
            StringResourceModel("$id$LABEL", this, null),
            Buttons.Type.Primary
        ) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                topics = sortedByOrder(topics, order.getObject())
                alignSortToIndex(topics)
                @Suppress("TooGenericExceptionCaught")
                try {
                    service.saveSortedNewsletterTopics(
                        this@NewsletterTopicSortPage.modelObject!!.id!!, topics
                    )
                    previousPageRef?.let { pageRef ->
                        setResponsePage(pageRef.page)
                    } ?: setResponsePage(PaperListPage::class.java)
                } catch (ex: Exception) {
                    error("Unexpected error: ${ex.message}")
                }
            }
        }

    private fun sortedByOrder(
        topics: List<NewsletterNewsletterTopic>,
        order: String?
    ): List<NewsletterNewsletterTopic> {
        val ids = order.orEmpty().split(",").mapNotNull { it.trim().toIntOrNull() }
        return topics.sortedBy { t -> ids.indexOf(t.newsletterTopicId).takeIf { it >= 0 } ?: Int.MAX_VALUE }
    }

    private fun alignSortToIndex(topics: List<NewsletterNewsletterTopic>) {
        var index = 0
        topics.forEach { t ->
            t.sort = index++
        }
    }

    private fun newCancelButton(id: String): BootstrapAjaxButton {
        return object : BootstrapAjaxButton(
            id,
            StringResourceModel(id + LABEL, this, null),
            Buttons.Type.Default
        ) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                if (previousPageRef != null)
                    setResponsePage(previousPageRef.page)
                else setResponsePage(PaperListPage::class.java)
            }
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val LABEL = ".label"

        private fun newListView(
            id: String,
            model: IModel<List<NewsletterNewsletterTopic>>
        ): ListView<NewsletterNewsletterTopic> =
            object : ListView<NewsletterNewsletterTopic>(id, model) {
                private val serialVersionUID: Long = 1L
                override fun populateItem(item: ListItem<NewsletterNewsletterTopic>) {
                    val topic = item.modelObject
                    item.add(Icon("icon", FontAwesome7IconTypeBuilder.FontAwesome7Solid.grip_vertical.fixed()))
                    item.add(Label("item", topic.displayValue ?: "n.a."))
                    item.add(AttributeModifier.replace("data-id", topic.newsletterTopicId))
                }
            }
    }
}
