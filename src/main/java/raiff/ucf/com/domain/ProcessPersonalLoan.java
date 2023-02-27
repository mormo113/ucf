package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessPersonalLoan.
 */
@Entity
@Table(name = "process_personal_loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessPersonalLoan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "reference")
    private String reference;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

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

    public ProcessPersonalLoan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public ProcessPersonalLoan requestType(String requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getReference() {
        return this.reference;
    }

    public ProcessPersonalLoan reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ProcessPersonalLoan firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ProcessPersonalLoan lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ProcessUpload getProcessUpload() {
        return this.processUpload;
    }

    public void setProcessUpload(ProcessUpload processUpload) {
        this.processUpload = processUpload;
    }

    public ProcessPersonalLoan processUpload(ProcessUpload processUpload) {
        this.setProcessUpload(processUpload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessPersonalLoan)) {
            return false;
        }
        return id != null && id.equals(((ProcessPersonalLoan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessPersonalLoan{" +
            "id=" + getId() +
            ", requestType='" + getRequestType() + "'" +
            ", reference='" + getReference() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
