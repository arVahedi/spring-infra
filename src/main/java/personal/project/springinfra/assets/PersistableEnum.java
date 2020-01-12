package personal.project.springinfra.assets;

/**
 * Created by gl4di4tor on 4/12/18.
 */
public interface PersistableEnum<E extends Enum<E> & PersistableEnum> {

    Integer getCode();

}
