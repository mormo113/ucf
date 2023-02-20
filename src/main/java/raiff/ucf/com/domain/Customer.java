package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "idun")
    private String idun;

    @Column(name = "first_name_rnet")
    private String firstNameRNET;

    @Column(name = "last_name_rnet")
    private String lastNameRNET;

    @Column(name = "first_name_pid")
    private String firstNamePID;

    @Column(name = "last_name_pid")
    private String lastNamePID;

    @Column(name = "nationality")
    private String nationality;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classification", "customer" }, allowSetters = true)
    private Set<File> files = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdun() {
        return this.idun;
    }

    public Customer idun(String idun) {
        this.setIdun(idun);
        return this;
    }

    public void setIdun(String idun) {
        this.idun = idun;
    }

    public String getFirstNameRNET() {
        return this.firstNameRNET;
    }

    public Customer firstNameRNET(String firstNameRNET) {
        this.setFirstNameRNET(firstNameRNET);
        return this;
    }

    public void setFirstNameRNET(String firstNameRNET) {
        this.firstNameRNET = firstNameRNET;
    }

    public String getLastNameRNET() {
        return this.lastNameRNET;
    }

    public Customer lastNameRNET(String lastNameRNET) {
        this.setLastNameRNET(lastNameRNET);
        return this;
    }

    public void setLastNameRNET(String lastNameRNET) {
        this.lastNameRNET = lastNameRNET;
    }

    public String getFirstNamePID() {
        return this.firstNamePID;
    }

    public Customer firstNamePID(String firstNamePID) {
        this.setFirstNamePID(firstNamePID);
        return this;
    }

    public void setFirstNamePID(String firstNamePID) {
        this.firstNamePID = firstNamePID;
    }

    public String getLastNamePID() {
        return this.lastNamePID;
    }

    public Customer lastNamePID(String lastNamePID) {
        this.setLastNamePID(lastNamePID);
        return this;
    }

    public void setLastNamePID(String lastNamePID) {
        this.lastNamePID = lastNamePID;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Customer nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<File> getFiles() {
        return this.files;
    }

    public void setFiles(Set<File> files) {
        if (this.files != null) {
            this.files.forEach(i -> i.setCustomer(null));
        }
        if (files != null) {
            files.forEach(i -> i.setCustomer(this));
        }
        this.files = files;
    }

    public Customer files(Set<File> files) {
        this.setFiles(files);
        return this;
    }

    public Customer addFile(File file) {
        this.files.add(file);
        file.setCustomer(this);
        return this;
    }

    public Customer removeFile(File file) {
        this.files.remove(file);
        file.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", idun='" + getIdun() + "'" +
            ", firstNameRNET='" + getFirstNameRNET() + "'" +
            ", lastNameRNET='" + getLastNameRNET() + "'" +
            ", firstNamePID='" + getFirstNamePID() + "'" +
            ", lastNamePID='" + getLastNamePID() + "'" +
            ", nationality='" + getNationality() + "'" +
            "}";
    }
}
