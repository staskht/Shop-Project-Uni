package uni.project.serviceContracts;

import java.io.IOException;

public interface IStorage<Model, DTO> {
    void save(Model domainObject, String folderPath) throws IOException;
    DTO load(String filePath)throws IOException;
}
