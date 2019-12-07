package personal.project.springinfra.model.domain;

public abstract class BaseDomain {

    private long id;

    //region Getter and Setter
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    //endregion
}
