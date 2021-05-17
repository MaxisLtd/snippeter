package com.mbeliakov.snippeter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mbeliakov.snippeter.domain.LangType} entity.
 */
public class LangTypeModel implements Serializable {

    private Long id;

    @NotNull
    private String code;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LangTypeModel)) {
            return false;
        }

        LangTypeModel langTypeModel = (LangTypeModel) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, langTypeModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LangTypeModel{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
