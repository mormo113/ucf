package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttachedFile.
 */
@Entity
@Table(name = "attached_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachedFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "ocr_raw")
    private String ocrRaw;

    @Column(name = "store_ged")
    private String storeGed;

    @JsonIgnoreProperties(value = { "attachedFile" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Classification classification;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "attachedFiles", "processMessagingRnet", "processIdentityDocument", "processInvoiceHomeLoan", "processPersonalLoan" },
        allowSetters = true
    )
    private ProcessUpload processUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttachedFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public AttachedFile fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public AttachedFile filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public AttachedFile documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getOcrRaw() {
        return this.ocrRaw;
    }

    public AttachedFile ocrRaw(String ocrRaw) {
        this.setOcrRaw(ocrRaw);
        return this;
    }

    public void setOcrRaw(String ocrRaw) {
        this.ocrRaw = ocrRaw;
    }

    public String getStoreGed() {
        return this.storeGed;
    }

    public AttachedFile storeGed(String storeGed) {
        this.setStoreGed(storeGed);
        return this;
    }

    public void setStoreGed(String storeGed) {
        this.storeGed = storeGed;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public AttachedFile classification(Classification classification) {
        this.setClassification(classification);
        return this;
    }

    public ProcessUpload getProcessUpload() {
        return this.processUpload;
    }

    public void setProcessUpload(ProcessUpload processUpload) {
        this.processUpload = processUpload;
    }

    public AttachedFile processUpload(ProcessUpload processUpload) {
        this.setProcessUpload(processUpload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachedFile)) {
            return false;
        }
        return id != null && id.equals(((AttachedFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachedFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", ocrRaw='" + getOcrRaw() + "'" +
            ", storeGed='" + getStoreGed() + "'" +
            "}";
    }
}
