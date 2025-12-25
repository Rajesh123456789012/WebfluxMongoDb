package com.info.service;

import com.info.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileUploadService {

    @Autowired
    private EmployeeService employeeService;

    public Flux<Employee> saveExcelData(FilePart filePart) throws IOException {

        Flux<DataBuffer> dataBufferFlux = filePart.content();

        // Convert the Flux of DataBuffers to a byte array
        byte[] bytes = dataBufferFlux.collectList().block().stream()
                .map(DataBuffer::asByteBuffer)
                .reduce(ByteBuffer::put)
                .orElse(ByteBuffer.allocate(0))
                .array();

        List<Employee> employeeArrayList = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
        XSSFSheet worksheet = workbook.getSheetAt(0);
        log.info("worksheet {} ", worksheet);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Employee employee = new Employee();

            XSSFRow row = worksheet.getRow(i);

            employee.setId(
                    Long.parseLong(getCellAsString(row.getCell(0)))
            );

            employee.setFirstName(getCellAsString(row.getCell(1)));
            employee.setLastName(getCellAsString(row.getCell(2)));
            employee.setAge(getCellAsString(row.getCell(3)));
            employee.setSalary(
                    Double.parseDouble(getCellAsString(row.getCell(4)))
            );

            employeeArrayList.add(employee);

        }
        log.info("Data {}", employeeArrayList);
        return Flux.fromIterable(employeeArrayList).flatMap(bpn -> employeeService.saveEmployee(bpn));
    }

    private String getCellAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula();

            default:
                return "";
        }
    }


}
