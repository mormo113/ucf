package raiff.ucf.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Classification.
 */
@Entity
@Table(name = "classification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "document_series_name")
    private String documentSeriesName;

    @Column(name = "classification_file_net")
    private String classificationFileNet;

    @Column(name = "n_trees_category")
    private String nTreesCategory;

    @Column(name = "n_two_category")
    private String nTwoCategory;

    @JsonIgnoreProperties(value = { "classification", "processUpload" }, allowSetters = true)
    @OneToOne(mappedBy = "classification")
    private AttachedFile attachedFile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSeriesName() {
        return this.documentSeriesName;
    }

    public Classification documentSeriesName(String documentSeriesName) {
        this.setDocumentSeriesName(documentSeriesName);
        return this;
    }

    public void setDocumentSeriesName(String documentSeriesName) {
        this.documentSeriesName = documentSeriesName;
    }

    public String getClassificationFileNet() {
        return this.classificationFileNet;
    }

    public Classification classificationFileNet(String classificationFileNet) {
        this.setClassificationFileNet(classificationFileNet);
        return this;
    }

    public void setClassificationFileNet(String classificationFileNet) {
        this.classificationFileNet = classificationFileNet;
    }

    public String getnTreesCategory() {
        return this.nTreesCategory;
    }

    public Classification nTreesCategory(String nTreesCategory) {
        this.setnTreesCategory(nTreesCategory);
        return this;
    }

    public void setnTreesCategory(String nTreesCategory) {
        this.nTreesCategory = nTreesCategory;
    }

    public String getnTwoCategory() {
        return this.nTwoCategory;
    }

    public Classification nTwoCategory(String nTwoCategory) {
        this.setnTwoCategory(nTwoCategory);
        return this;
    }

    public void setnTwoCategory(String nTwoCategory) {
        this.nTwoCategory = nTwoCategory;
    }

    public AttachedFile getAttachedFile() {
        return this.attachedFile;
    }

    public void setAttachedFile(AttachedFile attachedFile) {
        if (this.attachedFile != null) {
            this.attachedFile.setClassification(null);
        }
        if (attachedFile != null) {
            attachedFile.setClassification(this);
        }
        this.attachedFile = attachedFile;
    }

    public Classification attachedFile(AttachedFile attachedFile) {
        this.setAttachedFile(attachedFile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classification)) {
            return false;
        }
        return id != null && id.equals(((Classification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classification{" +
            "id=" + getId() +
            ", documentSeriesName='" + getDocumentSeriesName() + "'" +
            ", classificationFileNet='" + getClassificationFileNet() + "'" +
            ", nTreesCategory='" + getnTreesCategory() + "'" +
            ", nTwoCategory='" + getnTwoCategory() + "'" +
            "}";
    }
}
