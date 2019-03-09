package cn.itcast.core.service;


import java.io.IOException;
import java.sql.SQLException;

public interface UploadServiceCYH {
    void saveBrand() throws IOException, SQLException;
    void saveSpec() throws IOException, SQLException;
    void saveCate() throws IOException, SQLException;
    void saveTemp() throws IOException, SQLException;
}
