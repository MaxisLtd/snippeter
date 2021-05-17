package com.mbeliakov.snippeter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Snippet.
 */
@Entity
@Table(name = "snippet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Snippet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lang_type")
    private String langType;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Column(name = "cts", nullable = false)
    private Instant cts;

    @ManyToOne
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private ProjectChapter projectChapter;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Snippet id(Long id) {
        this.id = id;
        return this;
    }

    public String getLangType() {
        return this.langType;
    }

    public Snippet langType(String langType) {
        this.langType = langType;
        return this;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getTitle() {
        return this.title;
    }

    public Snippet title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Snippet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return this.text;
    }

    public Snippet text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCts() {
        return this.cts;
    }

    public Snippet cts(Instant cts) {
        this.cts = cts;
        return this;
    }

    public void setCts(Instant cts) {
        this.cts = cts;
    }

    public ProjectChapter getProjectChapter() {
        return this.projectChapter;
    }

    public Snippet projectChapter(ProjectChapter projectChapter) {
        this.setProjectChapter(projectChapter);
        return this;
    }

    public void setProjectChapter(ProjectChapter projectChapter) {
        this.projectChapter = projectChapter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Snippet)) {
            return false;
        }
        return id != null && id.equals(((Snippet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Snippet{" +
            "id=" + getId() +
            ", langType='" + getLangType() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", text='" + getText() + "'" +
            ", cts='" + getCts() + "'" +
            "}";
    }
}
