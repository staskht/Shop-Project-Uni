package uni.project.serviceContracts;

public interface IFileNameStrategy<T> {
    String getFileName(T object);
}
