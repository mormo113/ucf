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

    @Column(name = "nom_serie_document")
    private String nomSerieDocument;

    @Column(name = "classification_file_net")
    private String classificationFileNet;

    @Column(name = "n_trees_category")
    private String nTreesCategory;

    @Column(name = "n_two_category")
    private String nTwoCategory;

    @JsonIgnoreProperties(value = { "classification", "customer" }, allowSetters = true)
    @OneToOne(mappedBy = "classification")
    private File file;

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

    public String getNomSerieDocument() {
        return this.nomSerieDocument;
    }

    public Classification nomSerieDocument(String nomSerieDocument) {
        this.setNomSerieDocument(nomSerieDocument);
        return this;
    }

    public void setNomSerieDocument(String nomSerieDocument) {
        this.nomSerieDocument = nomSerieDocument;
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

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        if (this.file != null) {
            this.file.setClassification(null);
        }
        if (file != null) {
            file.setClassification(this);
        }
        this.file = file;
    }

    public Classification file(File file) {
        this.setFile(file);
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
            ", nomSerieDocument='" + getNomSerieDocument() + "'" +
            ", classificationFileNet='" + getClassificationFileNet() + "'" +
            ", nTreesCategory='" + getnTreesCategory() + "'" +
            ", nTwoCategory='" + getnTwoCategory() + "'" +
            "}";
    }
}
