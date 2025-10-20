package com.gadget.rental.rental;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.gadget.rental.exception.InvalidExcelFileException;

import jakarta.transaction.Transactional;

public class RentalGadgetStartupTask {

    private final RentalGadgetRepository rentalGadgetRepository;

    @Value("${rentalgadget.images.path}")
    private String imagesPath;

    @Value("${rentalgadget.excels.path}")
    private String excelsPath;

    public RentalGadgetStartupTask(RentalGadgetRepository rentalGadgetRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    @Scheduled(initialDelay = 0)
    @Transactional
    public void setupRentalGadgetListings() {
        try {
            List<Path> existingExcelPath = Files.find(Paths.get(String.format("%s/", excelsPath)), 1,
                    (path, _) -> {
                        return Files.isRegularFile(path) && FileSystems.getDefault()
                                .getPathMatcher("glob:*-current.xlsx").matches(path.getFileName());
                    }).collect(Collectors.toList());

            if (existingExcelPath.isEmpty() || existingExcelPath == null) {
                return;
            }

            File excelCopyFile = new File(existingExcelPath.get(0).toString());
            System.out.println(existingExcelPath.get(0).toString());

            Workbook workbook = WorkbookFactory.create(excelCopyFile);
            Sheet sheet = workbook.getSheetAt(0);

            long imagesDirCount = Files.list(Paths.get(String.format("%s/", imagesPath)))
                    .filter(Files::isDirectory)
                    .count();
            int imagesDirReachedCount = 0;
            int counter = 0;
            System.out.println("COUNT: " + imagesDirCount);

            // for (int i = 0; i < imagesDirCount + 1; ++i) {
            while (true) {
                Row currentRow = sheet.getRow(counter);
                int cellInARowCount = currentRow.getPhysicalNumberOfCells();

                System.out.println(cellInARowCount);
                if (cellInARowCount - 1 > 3) {
                    throw new InvalidExcelFileException("Excel file title is beyond the given count.");
                }

                Cell cellZero = currentRow.getCell(0);
                Cell cellOne = currentRow.getCell(1);
                Cell cellTwo = currentRow.getCell(2);

                if (cellZero == null || cellOne == null
                        || cellTwo == null) {
                    throw new InvalidExcelFileException("Excel file cell is null or listing number is invalid.");
                }

                if (counter == 0) {

                    if (cellZero.getCellType() != CellType.STRING
                            || cellOne.getCellType() != CellType.STRING
                            || cellTwo.getCellType() != CellType.STRING) {
                        throw new InvalidExcelFileException("Excel file title type is incorrect");
                    }

                    if (cellZero.getStringCellValue().toUpperCase().compareTo("PRODUCT_NAME") != 0) {
                        throw new InvalidExcelFileException("Excel file title \"PRODUCT_NAME\" is incorrect");
                    }

                    if (cellOne.getStringCellValue().toUpperCase().compareTo("PRODUCT_PRICE") != 0) {
                        throw new InvalidExcelFileException("Excel file title \"PRODUCT_PRICE\" is incorrect");
                    }

                    if (cellTwo.getStringCellValue().toUpperCase().compareTo("PRODUCT_DESCRIPTION") != 0) {
                        throw new InvalidExcelFileException("Excel file title \"PRODUCT_DESCRIPTION\" is incorrect");
                    }

                    counter++;
                    continue;
                }

                RentalGadgetModel rentalGadgetTemp = new RentalGadgetModel();

                if (cellZero.getCellType() != CellType.STRING) {
                    throw new InvalidExcelFileException("Excel file value \"PRODUCT_NAME\" is incorrect");
                }

                if (cellOne.getCellType() != CellType.NUMERIC) {
                    throw new InvalidExcelFileException("Excel file value \"PRODUCT_PRICE\" is incorrect");
                }

                if (cellTwo.getCellType() != CellType.STRING) {
                    throw new InvalidExcelFileException("Excel file value \"DESCRIPTION\" is incorrect");
                }

                counter++;

                String rentalGadgetListingName = String.join("-", cellZero.getStringCellValue().trim().split(" "))
                        .toUpperCase();

                Optional<RentalGadgetModel> exisitingRentalGadget = rentalGadgetRepository
                        .findRentalGadgetByName(rentalGadgetListingName);

                if (exisitingRentalGadget.isPresent()) {
                    continue;
                }

                if (!Files.isDirectory(Paths.get(String.format("%s/%s", imagesPath, rentalGadgetListingName)))) {
                    continue;
                }

                System.out.println(rentalGadgetListingName);
                rentalGadgetTemp.setName(rentalGadgetListingName);
                rentalGadgetTemp.setPrice(cellOne.getNumericCellValue());
                rentalGadgetTemp.setDescription(cellTwo.getStringCellValue());
                rentalGadgetTemp.setCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
                rentalGadgetTemp.setImageDir(String.format("%s/%s/", imagesPath, rentalGadgetListingName));

                rentalGadgetRepository.save(rentalGadgetTemp);

                imagesDirReachedCount++;

                if (imagesDirCount == imagesDirReachedCount) {
                    break;
                }
            }

        } catch (EncryptedDocumentException | IOException | InvalidExcelFileException e) {
            rentalGadgetRepository.deleteAll();

            try {
                Files.walk(Paths.get(String.format("%s/"), imagesPath)).forEach((path) -> {
                    try {
                        Files.walk(path).forEach((inner_path) -> {
                            try {
                                Files.delete(inner_path);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
