package uni.project.serviceContracts;

import java.io.BufferedReader;
import java.io.IOException;

public interface ITextReader<T> {
    T read(BufferedReader reader) throws IOException;
}
