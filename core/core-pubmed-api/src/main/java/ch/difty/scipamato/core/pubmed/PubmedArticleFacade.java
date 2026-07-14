package ch.difty.scipamato.core.pubmed;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.pubmed.api.PubmedArticle;
import ch.difty.scipamato.core.pubmed.api.PubmedBookArticle;

public interface PubmedArticleFacade {

    /**
     * Instantiate an instance of {@link ScipamatoPubmedArticle} if the provided
     * object is an instance of {@link PubmedArticle} or an instance of
     * {@link ScipamatoPubmedBookArticle} if the provided object is an instance of
     * {@link PubmedBookArticle}.
     *
     * @param pubmedArticleOrPubmedBookArticle
     *     and object that should either be an instance of
     *     {@link PubmedArticle} or {@link PubmedBookArticle}
     * @return a derivative of {@link AbstractPubmedArticleFacade}
     * @throws IllegalArgumentException
     *     - if the parameter is of any other class than one of the two
     *     managed ones.
     */
    static PubmedArticleFacade newPubmedArticleFrom(@NotNull final java.lang.Object pubmedArticleOrPubmedBookArticle) {
        if (pubmedArticleOrPubmedBookArticle instanceof final PubmedArticle pubmedArticle)
            return new ScipamatoPubmedArticle(pubmedArticle);
        else if (pubmedArticleOrPubmedBookArticle instanceof final PubmedBookArticle pubmedBookArticle)
            return new ScipamatoPubmedBookArticle(pubmedBookArticle);
        throw new IllegalArgumentException("Cannot instantiate ScipamatoArticle from provided object " + pubmedArticleOrPubmedBookArticle);
    }

    @Nullable
    String getPmId();

    @NotNull
    String getAuthors();

    @NotNull
    String getFirstAuthor();

    @NotNull
    String getPublicationYear();

    @NotNull
    String getLocation();

    @NotNull
    String getTitle();

    @NotNull
    String getDoi();

    @NotNull
    String getOriginalAbstract();
}
