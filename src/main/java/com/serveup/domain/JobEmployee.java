package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serveup.domain.enumeration.EmployeeType;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobEmployee.
 */
@Entity
@Table(name = "job_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeType type;

    @Column(name = "number")
    private Integer number;

    @Column(name = "found")
    private Integer found;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "employees", "comments", "ratings", "appUsers" }, allowSetters = true)
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobEmployee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeType getType() {
        return this.type;
    }

    public JobEmployee type(EmployeeType type) {
        this.setType(type);
        return this;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public Integer getNumber() {
        return this.number;
    }

    public JobEmployee number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFound() {
        return this.found;
    }

    public JobEmployee found(Integer found) {
        this.setFound(found);
        return this;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobEmployee job(Job job) {
        this.setJob(job);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobEmployee)) {
            return false;
        }
        return id != null && id.equals(((JobEmployee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobEmployee{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", number=" + getNumber() +
            ", found=" + getFound() +
            "}";
    }
}
