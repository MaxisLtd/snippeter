package com.mbeliakov.snippeter.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mbeliakov.snippeter.domain.Snippet} entity.
 */
public class SnippetModel implements Serializable {

    private Long id;

    private String langType;

    @NotNull
    private String title;

    private String description;

    @Lob
    private String text;

    @NotNull
    private Instant cts;

    private ProjectChapterModel projectChapter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCts() {
        return cts;
    }

    public void setCts(Instant cts) {
        this.cts = cts;
    }

    public ProjectChapterModel getProjectChapter() {
        return projectChapter;
    }

    public void setProjectChapter(ProjectChapterModel projectChapter) {
        this.projectChapter = projectChapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SnippetModel)) {
            return false;
        }

        SnippetModel snippetModel = (SnippetModel) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, snippetModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnippetModel{" +
            "id=" + getId() +
            ", langType='" + getLangType() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", text='" + getText() + "'" +
            ", cts='" + getCts() + "'" +
            ", projectChapter=" + getProjectChapter() +
            "}";
    }
}
