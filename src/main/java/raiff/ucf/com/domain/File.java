package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import raiff.ucf.com.domain.enumeration.TypeUC;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "pid_number")
    private String pidNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeUC type;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "demande_type")
    private String demandeType;

    @Column(name = "reference")
    private String reference;

    @Column(name = "message_rnet_title")
    private String messageRnetTitle;

    @Column(name = "communication")
    private String communication;

    @Column(name = "client_message")
    private String clientMessage;

    @Column(name = "ttc_amount")
    private String ttcAmount;

    @Column(name = "bic")
    private String bic;

    @Column(name = "iban")
    private String iban;

    @Column(name = "reception_date")
    private LocalDate receptionDate;

    @Column(name = "emission_date")
    private LocalDate emissionDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "modification_date")
    private LocalDate modificationDate;

    @Column(name = "store_ged")
    private Boolean storeGed;

    @JsonIgnoreProperties(value = { "file" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Classification classification;

    @ManyToOne
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPidNumber() {
        return this.pidNumber;
    }

    public File pidNumber(String pidNumber) {
        this.setPidNumber(pidNumber);
        return this;
    }

    public void setPidNumber(String pidNumber) {
        this.pidNumber = pidNumber;
    }

    public TypeUC getType() {
        return this.type;
    }

    public File type(TypeUC type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeUC type) {
        this.type = type;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public File documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDemandeType() {
        return this.demandeType;
    }

    public File demandeType(String demandeType) {
        this.setDemandeType(demandeType);
        return this;
    }

    public void setDemandeType(String demandeType) {
        this.demandeType = demandeType;
    }

    public String getReference() {
        return this.reference;
    }

    public File reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMessageRnetTitle() {
        return this.messageRnetTitle;
    }

    public File messageRnetTitle(String messageRnetTitle) {
        this.setMessageRnetTitle(messageRnetTitle);
        return this;
    }

    public void setMessageRnetTitle(String messageRnetTitle) {
        this.messageRnetTitle = messageRnetTitle;
    }

    public String getCommunication() {
        return this.communication;
    }

    public File communication(String communication) {
        this.setCommunication(communication);
        return this;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public File clientMessage(String clientMessage) {
        this.setClientMessage(clientMessage);
        return this;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getTtcAmount() {
        return this.ttcAmount;
    }

    public File ttcAmount(String ttcAmount) {
        this.setTtcAmount(ttcAmount);
        return this;
    }

    public void setTtcAmount(String ttcAmount) {
        this.ttcAmount = ttcAmount;
    }

    public String getBic() {
        return this.bic;
    }

    public File bic(String bic) {
        this.setBic(bic);
        return this;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getIban() {
        return this.iban;
    }

    public File iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public LocalDate getReceptionDate() {
        return this.receptionDate;
    }

    public File receptionDate(LocalDate receptionDate) {
        this.setReceptionDate(receptionDate);
        return this;
    }

    public void setReceptionDate(LocalDate receptionDate) {
        this.receptionDate = receptionDate;
    }

    public LocalDate getEmissionDate() {
        return this.emissionDate;
    }

    public File emissionDate(LocalDate emissionDate) {
        this.setEmissionDate(emissionDate);
        return this;
    }

    public void setEmissionDate(LocalDate emissionDate) {
        this.emissionDate = emissionDate;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public File expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public File creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return this.modificationDate;
    }

    public File modificationDate(LocalDate modificationDate) {
        this.setModificationDate(modificationDate);
        return this;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getStoreGed() {
        return this.storeGed;
    }

    public File storeGed(Boolean storeGed) {
        this.setStoreGed(storeGed);
        return this;
    }

    public void setStoreGed(Boolean storeGed) {
        this.storeGed = storeGed;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public File classification(Classification classification) {
        this.setClassification(classification);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public File customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", pidNumber='" + getPidNumber() + "'" +
            ", type='" + getType() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", demandeType='" + getDemandeType() + "'" +
            ", reference='" + getReference() + "'" +
            ", messageRnetTitle='" + getMessageRnetTitle() + "'" +
            ", communication='" + getCommunication() + "'" +
            ", clientMessage='" + getClientMessage() + "'" +
            ", ttcAmount='" + getTtcAmount() + "'" +
            ", bic='" + getBic() + "'" +
            ", iban='" + getIban() + "'" +
            ", receptionDate='" + getReceptionDate() + "'" +
            ", emissionDate='" + getEmissionDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", storeGed='" + getStoreGed() + "'" +
            "}";
    }
}
