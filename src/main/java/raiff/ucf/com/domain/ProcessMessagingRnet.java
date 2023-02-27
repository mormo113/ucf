package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessMessagingRnet.
 */
@Entity
@Table(name = "process_messaging_rnet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessMessagingRnet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "message_rnet_title")
    private String messageRnetTitle;

    @Column(name = "message_rnet_ref")
    private String messageRnetRef;

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

    public ProcessMessagingRnet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageRnetTitle() {
        return this.messageRnetTitle;
    }

    public ProcessMessagingRnet messageRnetTitle(String messageRnetTitle) {
        this.setMessageRnetTitle(messageRnetTitle);
        return this;
    }

    public void setMessageRnetTitle(String messageRnetTitle) {
        this.messageRnetTitle = messageRnetTitle;
    }

    public String getMessageRnetRef() {
        return this.messageRnetRef;
    }

    public ProcessMessagingRnet messageRnetRef(String messageRnetRef) {
        this.setMessageRnetRef(messageRnetRef);
        return this;
    }

    public void setMessageRnetRef(String messageRnetRef) {
        this.messageRnetRef = messageRnetRef;
    }

    public ProcessUpload getProcessUpload() {
        return this.processUpload;
    }

    public void setProcessUpload(ProcessUpload processUpload) {
        this.processUpload = processUpload;
    }

    public ProcessMessagingRnet processUpload(ProcessUpload processUpload) {
        this.setProcessUpload(processUpload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessMessagingRnet)) {
            return false;
        }
        return id != null && id.equals(((ProcessMessagingRnet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessMessagingRnet{" +
            "id=" + getId() +
            ", messageRnetTitle='" + getMessageRnetTitle() + "'" +
            ", messageRnetRef='" + getMessageRnetRef() + "'" +
            "}";
    }
}
