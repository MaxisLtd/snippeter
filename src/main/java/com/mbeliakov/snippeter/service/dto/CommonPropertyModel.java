package com.mbeliakov.snippeter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mbeliakov.snippeter.domain.CommonProperty} entity.
 */
public class CommonPropertyModel implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @Lob
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonPropertyModel)) {
            return false;
        }

        CommonPropertyModel commonPropertyModel = (CommonPropertyModel) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonPropertyModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonPropertyModel{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
