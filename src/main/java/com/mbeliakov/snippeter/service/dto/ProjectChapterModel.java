package com.mbeliakov.snippeter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mbeliakov.snippeter.domain.ProjectChapter} entity.
 */
public class ProjectChapterModel implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String type;

    private ProjectModel project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectChapterModel)) {
            return false;
        }

        ProjectChapterModel projectChapterModel = (ProjectChapterModel) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectChapterModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectChapterModel{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", project=" + getProject() +
            "}";
    }
}
