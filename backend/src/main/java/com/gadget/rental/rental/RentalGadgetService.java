package com.gadget.rental.rental;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;

import com.gadget.rental.exception.InvalidExcelFileException;
import com.gadget.rental.exception.InvalidImageFormatException;
import com.gadget.rental.exception.RentalGadgetExistedException;
import com.gadget.rental.exception.RentalGadgetImageExistedException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RentalGadgetService {

    @Value("${rentalgadget.images.path}")
    private String rootImagesPath;

    @Value("${rentalgadget.excels.path}")
    private String rootExcelsPath;

    @Value("${rentalgadget.misc.staging.path}")
    private String rootMiscStagingPath;

    @Value("${rentalgadget.images.staging.path}")
    private String rootImageStagingPath;

    private final RentalGadgetRepository rentalGadgetRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalGadgetService.class);
    private final int PAGE_SIZE = 16;

    @Autowired
    RentalGadgetService(RentalGadgetRepository rentalGadgetRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    public List<RentalGadgetModel> getListRentalGadget(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by(Sort.Order.asc("id")));
        Page<RentalGadgetModel> allRentalGadgetModel = rentalGadgetRepository.findAll(pageable);
        return allRentalGadgetModel.toList();
    }

    @Async
    public String addNewRentalGadget(RentalGadgetDTO rentalGadgetDTO) {
        String rentalGadgetListingName = String.join("-", rentalGadgetDTO.name().trim().split(" ")).toUpperCase();

        rentalGadgetRepository
                .findRentalGadgetByName(rentalGadgetListingName)
                .ifPresent((_) -> {
                    throw new RentalGadgetExistedException("Rental gadget listing has existed.");
                });

        try {
            RentalGadgetModel rentalGadgetTemp = new RentalGadgetModel();
            rentalGadgetTemp.setName(rentalGadgetListingName);
            rentalGadgetTemp.setDescription(rentalGadgetDTO.description());
            rentalGadgetTemp.setCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
            rentalGadgetTemp.setLastUpdated(rentalGadgetTemp.getCreatedAt());
            rentalGadgetTemp.setPrice(rentalGadgetDTO.price());

            Path directory = Files
                    .createDirectory(Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetListingName)));

            for (MultipartFile image : rentalGadgetDTO.images()) {
                if (!image.getContentType().startsWith("image")) {
                    LOGGER.error("Image format is invalid.");
                    throw new InvalidImageFormatException("Image format is invalid.");
                }

                String imageFilename = String.join("-", image.getOriginalFilename().trim().split(" ")).toUpperCase();
                Path imagePath = Files
                        .createFile(Paths
                                .get(String.format("%s/%s", directory.toString(), imageFilename)));
                Files.write(imagePath, image.getBytes());
            }

            rentalGadgetTemp.setImageDir(directory.toString());
            rentalGadgetRepository.save(rentalGadgetTemp);
            LOGGER.info(String.format("Rental gadget listing \"%s\" was added.", rentalGadgetDTO.name()));
            return String.format("Rental gadget listing \"%s\" was added.", rentalGadgetDTO.name());

        } catch (IOException e) {
            throw new RentalGadgetExistedException("Rental gadget listing has existed.");
        }
    }

    @Async
    public String removeExistingRentalGadget(Long id) {
        RentalGadgetModel rentalGadgetModel = rentalGadgetRepository.findById(id)
                .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
        rentalGadgetRepository.delete(rentalGadgetModel);

        Path imagePathOfListing = Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetModel.getName()));
        try {
            deleteDirectoryRecursivelyInclusive(imagePathOfListing);
        } catch (IOException e) {
            throw new RentalGadgetNotFoundException("Rental gadget listing is missing.");
        }
        LOGGER.info(String.format("Rental gadget listing \"%s\" was deleted.", rentalGadgetModel.getName()));
        return String.format("Rental gadget listing \"%s\" was deleted.", rentalGadgetModel.getName());
    }

    @Async
    @Transactional
    public String updateExistingRentalGadget(RentalGadgetDTO rentalGadgetDTO, Long id) {
        String rentalGadgetListingName = String.join("-", rentalGadgetDTO.name().trim().split(" ")).toUpperCase();

        RentalGadgetModel oldRentalGadgetModel = rentalGadgetRepository.findById(id)
                .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

        Path oldImagePathOfListing = Paths.get(String.format("%s/%s/", rootImagesPath, oldRentalGadgetModel.getName()));
        Path newImagePathOfListing = Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetListingName));
        try {
            if (oldImagePathOfListing.compareTo(newImagePathOfListing) != 0) {
                oldRentalGadgetModel.setName(rentalGadgetListingName);
                Files.move(oldImagePathOfListing, newImagePathOfListing);
                oldRentalGadgetModel.setImageDir(newImagePathOfListing.toString());
            }

            if (oldRentalGadgetModel.getPrice() == rentalGadgetDTO.price()) {
                oldRentalGadgetModel.setPrice(rentalGadgetDTO.price());
            }

            if (oldRentalGadgetModel.getDescription() == rentalGadgetDTO.description()) {
                oldRentalGadgetModel.setDescription(rentalGadgetDTO.description());
            }

            oldRentalGadgetModel.setLastUpdated(ZonedDateTime.now(ZoneId.of("Z")));
            rentalGadgetRepository.save(oldRentalGadgetModel);

        } catch (IOException e) {
            throw new RentalGadgetNotFoundException("Rental gadget listing is missing.");
        }
        LOGGER.info(String.format("Rental gadget listing \"%s\" was updated.", oldRentalGadgetModel.getName()));
        return String.format("Rental gadget listing \"%s\" was updated.", oldRentalGadgetModel.getName());
    }

    @Async
    @Transactional
    public List<String> batchUpdateAndAppendNewRentalGadgetListing(MultipartFile excelFile,
            int numberOfRentalGadgetListings) {
        List<String> allListingNames = new ArrayList<>();

        if (excelFile.getContentType()
                .compareTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") != 0) {
            throw new InvalidExcelFileException("Excel file given is invalid.");
        }

        try {
            Path imageBackupPath = Paths.get(String.format("%s/", rootImageStagingPath));
            deleteDirectoryRecursively(Paths.get(String.format("%s/", rootImageStagingPath)));
            copyBackup(Paths.get(String.format("%s/", rootImagesPath)), imageBackupPath);
            deleteDirectoryRecursively(Paths.get(String.format("%s/", rootImagesPath)));

            Workbook workbook = WorkbookFactory.create(excelFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < (numberOfRentalGadgetListings + 1); ++i) {
                Row currentRow = sheet.getRow(i);
                int cellInARowCount = currentRow.getPhysicalNumberOfCells();

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

                if (i == 0) {

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

                    continue;
                }

                if (cellZero.getCellType() != CellType.STRING) {
                    throw new InvalidExcelFileException("Excel file value \"PRODUCT_NAME\" is incorrect");
                }

                if (cellOne.getCellType() != CellType.NUMERIC) {
                    throw new InvalidExcelFileException("Excel file value \"PRODUCT_PRICE\" is incorrect");
                }

                if (cellTwo.getCellType() != CellType.STRING) {
                    throw new InvalidExcelFileException("Excel file value \"DESCRIPTION\" is incorrect");
                }

                String rentalGadgetListingName = String.join("-", cellZero.getStringCellValue().trim().split(" "))
                        .toUpperCase();

                Optional<RentalGadgetModel> existingListing = rentalGadgetRepository
                        .findRentalGadgetByName(rentalGadgetListingName);

                if (existingListing.isPresent()) {
                    existingListing.get().setPrice(cellOne.getNumericCellValue());
                    existingListing.get().setDescription(cellTwo.getStringCellValue());
                    existingListing.get().setLastUpdated(ZonedDateTime.now(ZoneId.of("Z")));
                    rentalGadgetRepository.save(existingListing.get());
                    continue;
                }

                RentalGadgetModel rentalGadgetTemp = new RentalGadgetModel();
                Path directory = Files
                        .createDirectory(Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetListingName)));
                allListingNames.add(cellZero.getStringCellValue());

                rentalGadgetTemp.setName(rentalGadgetListingName);
                rentalGadgetTemp.setPrice(cellOne.getNumericCellValue());
                rentalGadgetTemp.setDescription(cellTwo.getStringCellValue());
                rentalGadgetTemp.setCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
                rentalGadgetTemp.setImageDir(directory.toString());

                rentalGadgetRepository.save(rentalGadgetTemp);
            }

            Stream<Path> existingExcelPath = Files.find(Paths.get(String.format("%s/", rootExcelsPath)), 1,
                    (path, _) -> {
                        return Files.isRegularFile(path) && FileSystems.getDefault()
                                .getPathMatcher("glob:*-current.xlsx").matches(path.getFileName());
                    });
            existingExcelPath.forEach((path) -> {
                int index = path.toString().lastIndexOf("current");

                Path newPath = Paths
                        .get(String.join("", path.toString().substring(0, index - 1),
                                path.toString().substring(index + 7)));
                try {
                    Files.move(path, newPath);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            existingExcelPath.close();

            Path excelPath = Files.createFile(
                    Paths.get(String.format("%s/%s-current.xlsx", rootExcelsPath,
                            ZonedDateTime.now(ZoneId.of("Z")).toString())));
            File excelCopyFile = new File(excelPath.toString());
            excelFile.transferTo(excelCopyFile);

        } catch (FileAlreadyExistsException e) {
            LOGGER.error("An error occured, rolling back.");
            try {
                deleteDirectoryRecursively(Paths.get(String.format("%s/", rootImagesPath)));
                Path imageBackupPath = Paths.get(String.format("%s/", rootImageStagingPath));
                copyBackup(imageBackupPath, Paths.get(String.format("%s/", rootImagesPath)));
            } catch (IOException e1) {
                LOGGER.error("An error occured in i/o operation.");
            }
            throw new RentalGadgetExistedException("Rental gadget listing has existed.");

        } catch (EncryptedDocumentException | InvalidExcelFileException | IOException e) {
            LOGGER.error("An error occured, rolling back.");
            try {
                deleteDirectoryRecursively(Paths.get(String.format("%s/", rootImagesPath)));
                Path imageBackupPath = Paths.get(String.format("%s/", rootImageStagingPath));
                copyBackup(imageBackupPath, Paths.get(String.format("%s/", rootImagesPath)));
            } catch (IOException e1) {
                LOGGER.error("An error occured in i/o operation.");
            }
            LOGGER.error("Excel file is invalid.");
            throw new InvalidExcelFileException(e.getMessage());
        }
        return allListingNames;
    }

    @Async
    public String uploadImagesToDirectory(MultipartFile[] images, Long id) {
        try {
            RentalGadgetModel existingListing = rentalGadgetRepository
                    .findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

            String rentalGadgetListingName = existingListing.getName();
            Path directory = Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetListingName));

            for (MultipartFile image : images) {
                if (!image.getContentType().startsWith("image")) {
                    LOGGER.error("Image format is invalid.");
                    throw new InvalidImageFormatException("Image format is invalid.");
                }

                String imageFilename = String.join("-", image.getOriginalFilename().trim().split(" ")).toUpperCase();
                Path imagePath = Files
                        .createFile(Paths
                                .get(String.format("%s/%s", directory.toString(), imageFilename)));
                Files.write(imagePath, image.getBytes());
            }
            return String.format("Uploading images to \"%s\" is successful.", rentalGadgetListingName);

        } catch (IOException e) {
            LOGGER.error("Rental gadget image has existed.");
            throw new RentalGadgetImageExistedException("Rental gadget image has existed.");
        }
    }

    @Async
    public String deleteImagesFromDirectory(Long id) {
        RentalGadgetModel rentalGadgetModel = rentalGadgetRepository.findById(id)
                .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

        Path imagePathOfListing = Paths.get(String.format("%s/%s/", rootImagesPath, rentalGadgetModel.getName()));
        try {
            deleteDirectoryRecursively(imagePathOfListing);
        } catch (IOException e) {
            throw new RentalGadgetNotFoundException("Rental gadget listing is missing.");
        }
        LOGGER.info(String.format("Rental gadget listing \"%s\" was deleted.", rentalGadgetModel.getName()));
        return String.format("Rental gadget listing \"%s\" was deleted.", rentalGadgetModel.getName());
    }

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (dir.toString().compareTo(path.toString()) == 0) {
                    return FileVisitResult.CONTINUE;
                }

                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void deleteDirectoryRecursivelyInclusive(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copyBackup(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
