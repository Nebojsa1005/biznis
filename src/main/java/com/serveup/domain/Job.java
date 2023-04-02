package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serveup.domain.enumeration.JobType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "dificulty")
    private Double dificulty;

    @Column(name = "date_from")
    private Instant dateFrom;

    @Column(name = "date_to")
    private Instant dateTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_job")
    private JobType typeOfJob;

    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Set<JobEmployee> employees = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_job__comment", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "comment_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUsers", "jobs" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_job__rating", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "rating_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUsers", "jobs" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @ManyToMany(mappedBy = "jobs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "comments", "ratings" }, allowSetters = true)
    private Set<AppUser> appUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Job id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Job title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Job description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDificulty() {
        return this.dificulty;
    }

    public Job dificulty(Double dificulty) {
        this.setDificulty(dificulty);
        return this;
    }

    public void setDificulty(Double dificulty) {
        this.dificulty = dificulty;
    }

    public Instant getDateFrom() {
        return this.dateFrom;
    }

    public Job dateFrom(Instant dateFrom) {
        this.setDateFrom(dateFrom);
        return this;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return this.dateTo;
    }

    public Job dateTo(Instant dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Job isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public JobType getJobType() {
        return this.jobType;
    }

    public Job jobType(JobType jobType) {
        this.setJobType(jobType);
        return this;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobType getTypeOfJob() {
        return this.typeOfJob;
    }

    public Job typeOfJob(JobType typeOfJob) {
        this.setTypeOfJob(typeOfJob);
        return this;
    }

    public void setTypeOfJob(JobType typeOfJob) {
        this.typeOfJob = typeOfJob;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Job location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Set<JobEmployee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<JobEmployee> jobEmployees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setJob(null));
        }
        if (jobEmployees != null) {
            jobEmployees.forEach(i -> i.setJob(this));
        }
        this.employees = jobEmployees;
    }

    public Job employees(Set<JobEmployee> jobEmployees) {
        this.setEmployees(jobEmployees);
        return this;
    }

    public Job addEmployees(JobEmployee jobEmployee) {
        this.employees.add(jobEmployee);
        jobEmployee.setJob(this);
        return this;
    }

    public Job removeEmployees(JobEmployee jobEmployee) {
        this.employees.remove(jobEmployee);
        jobEmployee.setJob(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Job comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Job addComment(Comment comment) {
        this.comments.add(comment);
        comment.getJobs().add(this);
        return this;
    }

    public Job removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.getJobs().remove(this);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Job ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Job addRating(Rating rating) {
        this.ratings.add(rating);
        rating.getJobs().add(this);
        return this;
    }

    public Job removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.getJobs().remove(this);
        return this;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.removeJob(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addJob(this));
        }
        this.appUsers = appUsers;
    }

    public Job appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public Job addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.getJobs().add(this);
        return this;
    }

    public Job removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.getJobs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dificulty=" + getDificulty() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", jobType='" + getJobType() + "'" +
            ", typeOfJob='" + getTypeOfJob() + "'" +
            "}";
    }
}
