package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessIdentityDocument.
 */
@Entity
@Table(name = "process_identity_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessIdentityDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_name_pid")
    private String lastNamePid;

    @Column(name = "first_name_pid")
    private String firstNamePid;

    @Column(name = "nationality_pid")
    private String nationalityPid;

    @Column(name = "number_pid")
    private String numberPid;

    @Column(name = "expiration_date_pid")
    private LocalDate expirationDatePid;

    @Column(name = "issue_date_pid")
    private LocalDate issueDatePid;

    @JsonIgnoreProperties(
        value = { "attachedFiles", "processMessagingRnet", "processIdentityDocument", "processInvoiceHomeLoan", "processPersonalLoan" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private ProcessUpload processUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcessIdentityDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastNamePid() {
        return this.lastNamePid;
    }

    public ProcessIdentityDocument lastNamePid(String lastNamePid) {
        this.setLastNamePid(lastNamePid);
        return this;
    }

    public void setLastNamePid(String lastNamePid) {
        this.lastNamePid = lastNamePid;
    }

    public String getFirstNamePid() {
        return this.firstNamePid;
    }

    public ProcessIdentityDocument firstNamePid(String firstNamePid) {
        this.setFirstNamePid(firstNamePid);
        return this;
    }

    public void setFirstNamePid(String firstNamePid) {
        this.firstNamePid = firstNamePid;
    }

    public String getNationalityPid() {
        return this.nationalityPid;
    }

    public ProcessIdentityDocument nationalityPid(String nationalityPid) {
        this.setNationalityPid(nationalityPid);
        return this;
    }

    public void setNationalityPid(String nationalityPid) {
        this.nationalityPid = nationalityPid;
    }

    public String getNumberPid() {
        return this.numberPid;
    }

    public ProcessIdentityDocument numberPid(String numberPid) {
        this.setNumberPid(numberPid);
        return this;
    }

    public void setNumberPid(String numberPid) {
        this.numberPid = numberPid;
    }

    public LocalDate getExpirationDatePid() {
        return this.expirationDatePid;
    }

    public ProcessIdentityDocument expirationDatePid(LocalDate expirationDatePid) {
        this.setExpirationDatePid(expirationDatePid);
        return this;
    }

    public void setExpirationDatePid(LocalDate expirationDatePid) {
        this.expirationDatePid = expirationDatePid;
    }

    public LocalDate getIssueDatePid() {
        return this.issueDatePid;
    }

    public ProcessIdentityDocument issueDatePid(LocalDate issueDatePid) {
        this.setIssueDatePid(issueDatePid);
        return this;
    }

    public void setIssueDatePid(LocalDate issueDatePid) {
        this.issueDatePid = issueDatePid;
    }

    public ProcessUpload getProcessUpload() {
        return this.processUpload;
    }

    public void setProcessUpload(ProcessUpload processUpload) {
        this.processUpload = processUpload;
    }

    public ProcessIdentityDocument processUpload(ProcessUpload processUpload) {
        this.setProcessUpload(processUpload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessIdentityDocument)) {
            return false;
        }
        return id != null && id.equals(((ProcessIdentityDocument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessIdentityDocument{" +
            "id=" + getId() +
            ", lastNamePid='" + getLastNamePid() + "'" +
            ", firstNamePid='" + getFirstNamePid() + "'" +
            ", nationalityPid='" + getNationalityPid() + "'" +
            ", numberPid='" + getNumberPid() + "'" +
            ", expirationDatePid='" + getExpirationDatePid() + "'" +
            ", issueDatePid='" + getIssueDatePid() + "'" +
            "}";
    }
}
