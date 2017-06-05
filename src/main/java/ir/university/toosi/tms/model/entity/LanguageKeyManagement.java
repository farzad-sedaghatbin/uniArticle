package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_LanguageKeyManagement")
@NamedQueries({
        @NamedQuery(
                name = "LanguageKeyManagement.list",
                query = "select l from LanguageKeyManagement l where l.deleted='0'"
        ),
        @NamedQuery(
                name = "LanguageKeyManagement.findByDescKey",
                query = "select l from LanguageKeyManagement l where l.descriptionKey=:descriptionKey and l.deleted='0'"
        ),
        @NamedQuery(
                name = "LanguageKeyManagement.findById",
                query = "select l from LanguageKeyManagement l where l.id=:id and l.deleted='0'"
        ), @NamedQuery(
        name = "LanguageKeyManagement.maximum",
        query = "select max(l.id) from LanguageKeyManagement l"
)
})

public class LanguageKeyManagement extends BaseEntity {

    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;
    @Column(name = "description_key")
    @JsonProperty
    private String descriptionKey;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_LNGKEYVALUE")
    @JsonProperty
    private Set<LanguageManagement> languageManagements;


    public LanguageKeyManagement(String descriptionKey, Set<LanguageManagement> languageManagements) {
        this.descriptionKey = descriptionKey;
        this.languageManagements = languageManagements;
    }

    public LanguageKeyManagement(Set<LanguageManagement> languageManagements) {
        this.languageManagements = languageManagements;
    }

    public LanguageKeyManagement() {
    }

    public LanguageKeyManagement(long id, String descriptionKey, HashSet<LanguageManagement> languageManagements) {
        this.id = id;
        this.descriptionKey = descriptionKey;
        this.languageManagements = languageManagements;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public Set<LanguageManagement> getLanguageManagements() {
        return languageManagements;
    }

    public void setLanguageManagements(Set<LanguageManagement> languageManagements) {
        this.languageManagements = languageManagements;
    }
}
