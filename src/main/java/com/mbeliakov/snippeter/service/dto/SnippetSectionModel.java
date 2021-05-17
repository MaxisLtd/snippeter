package com.mbeliakov.snippeter.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mbeliakov.snippeter.domain.SnippetSection} entity.
 */
public class SnippetSectionModel implements Serializable {

    private Long id;

    private String langType;

    @NotNull
    private String title;

    private String description;

    @Lob
    private String text;

    @NotNull
    private Instant cts;

    @NotNull
    private Integer orderPosition;

    @NotNull
    private Boolean status;

    private SnippetModel snippet;

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

    public Integer getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SnippetModel getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetModel snippet) {
        this.snippet = snippet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SnippetSectionModel)) {
            return false;
        }

        SnippetSectionModel snippetSectionModel = (SnippetSectionModel) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, snippetSectionModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnippetSectionModel{" +
            "id=" + getId() +
            ", langType='" + getLangType() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", text='" + getText() + "'" +
            ", cts='" + getCts() + "'" +
            ", orderPosition=" + getOrderPosition() +
            ", status='" + getStatus() + "'" +
            ", snippet=" + getSnippet() +
            "}";
    }
}
