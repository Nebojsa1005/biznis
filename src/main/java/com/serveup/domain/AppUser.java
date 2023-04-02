package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serveup.domain.enumeration.UserType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private Long phone;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "avg_salary")
    private Double avgSalary;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @ManyToMany
    @JoinTable(
        name = "rel_app_user__job",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "employees", "comments", "ratings", "appUsers" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_app_user__comment",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUsers", "jobs" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_app_user__rating",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "rating_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUsers", "jobs" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AppUser id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public AppUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AppUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return this.phone;
    }

    public AppUser phone(Long phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return this.image;
    }

    public AppUser image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public AppUser imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Double getAvgSalary() {
        return this.avgSalary;
    }

    public AppUser avgSalary(Double avgSalary) {
        this.setAvgSalary(avgSalary);
        return this;
    }

    public void setAvgSalary(Double avgSalary) {
        this.avgSalary = avgSalary;
    }

    public Double getAvgRating() {
        return this.avgRating;
    }

    public AppUser avgRating(Double avgRating) {
        this.setAvgRating(avgRating);
        return this;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppUser isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public AppUser userType(UserType userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public AppUser jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public AppUser addJob(Job job) {
        this.jobs.add(job);
        job.getAppUsers().add(this);
        return this;
    }

    public AppUser removeJob(Job job) {
        this.jobs.remove(job);
        job.getAppUsers().remove(this);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public AppUser comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public AppUser addComment(Comment comment) {
        this.comments.add(comment);
        comment.getAppUsers().add(this);
        return this;
    }

    public AppUser removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.getAppUsers().remove(this);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public AppUser ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public AppUser addRating(Rating rating) {
        this.ratings.add(rating);
        rating.getAppUsers().add(this);
        return this;
    }

    public AppUser removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.getAppUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone=" + getPhone() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", avgSalary=" + getAvgSalary() +
            ", avgRating=" + getAvgRating() +
            ", isActive='" + getIsActive() + "'" +
            ", userType='" + getUserType() + "'" +
            "}";
    }
}
