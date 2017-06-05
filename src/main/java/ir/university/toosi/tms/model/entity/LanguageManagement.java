package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "TB_LanguageManagement")
@NamedQueries({
        @NamedQuery(
                name = "LanguageManagement.list",
                query = "select l from LanguageManagement l where  l.deleted='0'"
        ),
        @NamedQuery(
                name = "LanguageManagement.findById",
                query = "select l from LanguageManagement l where l.id=:id and l.deleted='0'"
        ), @NamedQuery(
        name = "LanguageManagement.maximum",
        query = "select max(l.id) from LanguageManagement l"
)
})

public class LanguageManagement extends BaseEntity {

    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;
    @Column(name = "title")
    @JsonProperty
    private String title;
    @ManyToOne
    @JsonProperty
    private Languages type;

    public LanguageManagement() {
    }

    public LanguageManagement(long id, String title, Languages type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Languages getType() {
        return type;
    }

    public void setType(Languages type) {
        this.type = type;
    }
}
