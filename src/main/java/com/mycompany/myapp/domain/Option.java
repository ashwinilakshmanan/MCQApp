package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "option")
    private String option;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @ManyToOne
    @JsonIgnoreProperties(value = { "options", "category" }, allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Option id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return this.option;
    }

    public Option option(String option) {
        this.setOption(option);
        return this;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Boolean getIsCorrect() {
        return this.isCorrect;
    }

    public Option isCorrect(Boolean isCorrect) {
        this.setIsCorrect(isCorrect);
        return this;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option question(Question question) {
        this.setQuestion(question);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", option='" + getOption() + "'" +
            ", isCorrect='" + getIsCorrect() + "'" +
            "}";
    }
}
