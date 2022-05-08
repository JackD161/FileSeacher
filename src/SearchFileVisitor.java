import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {
    private String partOfName;
    private String partOfContent;
    private int minSize;
    private int maxSize;
    private boolean checkMaxSize;
    private boolean checkMinSize;
    private boolean checkPartOfName;
    private boolean checkPartOfContent;
    private static final List<Path> foundFiles = new ArrayList<>();

    public SearchFileVisitor() {
        partOfName = null;
        partOfContent = null;
        minSize = 0;
        maxSize = 0;
        checkMinSize = false;
        checkMaxSize = false;
        checkPartOfName = false;
        checkPartOfContent = false;
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
        this.checkPartOfName = true;
    }

    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
        this.checkPartOfContent = true;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
        this.checkMinSize = true;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        this.checkMaxSize = true;
    }

    public List<Path> getFoundFiles() {
        return foundFiles;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        byte[] content = Files.readAllBytes(file);

        if (!attrs.isRegularFile()) {
            return CONTINUE;
        }

        if (checkMinSize && attrs.size() < minSize) {
            return CONTINUE;
        }

        if (checkMaxSize && attrs.size() > maxSize) {
            return CONTINUE;
        }

        if (checkPartOfContent && !new String(content).contains(partOfContent)) {
            return CONTINUE;
        }

        if (checkPartOfName && !file.getFileName().toString().contains(partOfName)) {
            return CONTINUE;
        }

        foundFiles.add(file);

        return CONTINUE;
    }
}
