package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessUpload.
 */
@Entity
@Table(name = "process_upload")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "process_state")
    private String processState;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @Column(name = "reception_date")
    private LocalDate receptionDate;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "processUpload")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classification", "processUpload" }, allowSetters = true)
    private Set<AttachedFile> attachedFiles = new HashSet<>();

    @JsonIgnoreProperties(value = { "processUpload" }, allowSetters = true)
    @OneToOne(mappedBy = "processUpload")
    private ProcessMessagingRnet processMessagingRnet;

    @JsonIgnoreProperties(value = { "processUpload" }, allowSetters = true)
    @OneToOne(mappedBy = "processUpload")
    private ProcessIdentityDocument processIdentityDocument;

    @JsonIgnoreProperties(value = { "processUpload" }, allowSetters = true)
    @OneToOne(mappedBy = "processUpload")
    private ProcessInvoiceHomeLoan processInvoiceHomeLoan;

    @JsonIgnoreProperties(value = { "processUpload" }, allowSetters = true)
    @OneToOne(mappedBy = "processUpload")
    private ProcessPersonalLoan processPersonalLoan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcessUpload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessState() {
        return this.processState;
    }

    public ProcessUpload processState(String processState) {
        this.setProcessState(processState);
        return this;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public ProcessUpload customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public ProcessUpload creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public ProcessUpload lastUpdateDate(LocalDate lastUpdateDate) {
        this.setLastUpdateDate(lastUpdateDate);
        return this;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDate getReceptionDate() {
        return this.receptionDate;
    }

    public ProcessUpload receptionDate(LocalDate receptionDate) {
        this.setReceptionDate(receptionDate);
        return this;
    }

    public void setReceptionDate(LocalDate receptionDate) {
        this.receptionDate = receptionDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public ProcessUpload notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<AttachedFile> getAttachedFiles() {
        return this.attachedFiles;
    }

    public void setAttachedFiles(Set<AttachedFile> attachedFiles) {
        if (this.attachedFiles != null) {
            this.attachedFiles.forEach(i -> i.setProcessUpload(null));
        }
        if (attachedFiles != null) {
            attachedFiles.forEach(i -> i.setProcessUpload(this));
        }
        this.attachedFiles = attachedFiles;
    }

    public ProcessUpload attachedFiles(Set<AttachedFile> attachedFiles) {
        this.setAttachedFiles(attachedFiles);
        return this;
    }

    public ProcessUpload addAttachedFile(AttachedFile attachedFile) {
        this.attachedFiles.add(attachedFile);
        attachedFile.setProcessUpload(this);
        return this;
    }

    public ProcessUpload removeAttachedFile(AttachedFile attachedFile) {
        this.attachedFiles.remove(attachedFile);
        attachedFile.setProcessUpload(null);
        return this;
    }

    public ProcessMessagingRnet getProcessMessagingRnet() {
        return this.processMessagingRnet;
    }

    public void setProcessMessagingRnet(ProcessMessagingRnet processMessagingRnet) {
        if (this.processMessagingRnet != null) {
            this.processMessagingRnet.setProcessUpload(null);
        }
        if (processMessagingRnet != null) {
            processMessagingRnet.setProcessUpload(this);
        }
        this.processMessagingRnet = processMessagingRnet;
    }

    public ProcessUpload processMessagingRnet(ProcessMessagingRnet processMessagingRnet) {
        this.setProcessMessagingRnet(processMessagingRnet);
        return this;
    }

    public ProcessIdentityDocument getProcessIdentityDocument() {
        return this.processIdentityDocument;
    }

    public void setProcessIdentityDocument(ProcessIdentityDocument processIdentityDocument) {
        if (this.processIdentityDocument != null) {
            this.processIdentityDocument.setProcessUpload(null);
        }
        if (processIdentityDocument != null) {
            processIdentityDocument.setProcessUpload(this);
        }
        this.processIdentityDocument = processIdentityDocument;
    }

    public ProcessUpload processIdentityDocument(ProcessIdentityDocument processIdentityDocument) {
        this.setProcessIdentityDocument(processIdentityDocument);
        return this;
    }

    public ProcessInvoiceHomeLoan getProcessInvoiceHomeLoan() {
        return this.processInvoiceHomeLoan;
    }

    public void setProcessInvoiceHomeLoan(ProcessInvoiceHomeLoan processInvoiceHomeLoan) {
        if (this.processInvoiceHomeLoan != null) {
            this.processInvoiceHomeLoan.setProcessUpload(null);
        }
        if (processInvoiceHomeLoan != null) {
            processInvoiceHomeLoan.setProcessUpload(this);
        }
        this.processInvoiceHomeLoan = processInvoiceHomeLoan;
    }

    public ProcessUpload processInvoiceHomeLoan(ProcessInvoiceHomeLoan processInvoiceHomeLoan) {
        this.setProcessInvoiceHomeLoan(processInvoiceHomeLoan);
        return this;
    }

    public ProcessPersonalLoan getProcessPersonalLoan() {
        return this.processPersonalLoan;
    }

    public void setProcessPersonalLoan(ProcessPersonalLoan processPersonalLoan) {
        if (this.processPersonalLoan != null) {
            this.processPersonalLoan.setProcessUpload(null);
        }
        if (processPersonalLoan != null) {
            processPersonalLoan.setProcessUpload(this);
        }
        this.processPersonalLoan = processPersonalLoan;
    }

    public ProcessUpload processPersonalLoan(ProcessPersonalLoan processPersonalLoan) {
        this.setProcessPersonalLoan(processPersonalLoan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessUpload)) {
            return false;
        }
        return id != null && id.equals(((ProcessUpload) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessUpload{" +
            "id=" + getId() +
            ", processState='" + getProcessState() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdateDate='" + getLastUpdateDate() + "'" +
            ", receptionDate='" + getReceptionDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
