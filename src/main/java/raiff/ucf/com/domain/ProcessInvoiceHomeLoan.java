package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessInvoiceHomeLoan.
 */
@Entity
@Table(name = "process_invoice_home_loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessInvoiceHomeLoan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ttc_amount")
    private String ttcAmount;

    @Column(name = "bic")
    private String bic;

    @Column(name = "iban")
    private String iban;

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

    public ProcessInvoiceHomeLoan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTtcAmount() {
        return this.ttcAmount;
    }

    public ProcessInvoiceHomeLoan ttcAmount(String ttcAmount) {
        this.setTtcAmount(ttcAmount);
        return this;
    }

    public void setTtcAmount(String ttcAmount) {
        this.ttcAmount = ttcAmount;
    }

    public String getBic() {
        return this.bic;
    }

    public ProcessInvoiceHomeLoan bic(String bic) {
        this.setBic(bic);
        return this;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getIban() {
        return this.iban;
    }

    public ProcessInvoiceHomeLoan iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public ProcessUpload getProcessUpload() {
        return this.processUpload;
    }

    public void setProcessUpload(ProcessUpload processUpload) {
        this.processUpload = processUpload;
    }

    public ProcessInvoiceHomeLoan processUpload(ProcessUpload processUpload) {
        this.setProcessUpload(processUpload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessInvoiceHomeLoan)) {
            return false;
        }
        return id != null && id.equals(((ProcessInvoiceHomeLoan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessInvoiceHomeLoan{" +
            "id=" + getId() +
            ", ttcAmount='" + getTtcAmount() + "'" +
            ", bic='" + getBic() + "'" +
            ", iban='" + getIban() + "'" +
            "}";
    }
}
