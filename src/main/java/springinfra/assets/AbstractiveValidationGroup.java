package springinfra.assets;

@FunctionalInterface
public interface AbstractiveValidationGroup {

    Class<?>[] getGroups(Object object);
}
