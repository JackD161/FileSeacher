import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainFrame {
    private final JFrame window;
    private SearchFileVisitor searchFileVisitor;
    private final JTextArea result;
    private final JTextField partOfName;
    private final JTextField partOfContent;
    private final JTextField maxSize;
    private final JTextField minSize;
    private final JTextField patch;


    public MainFrame()
    {
        searchFileVisitor = new SearchFileVisitor();
        window = new JFrame("Расширенный поис файлов");
        window.setBounds(200, 300, 900, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        result = new JTextArea(30, 45);
        partOfName = new JTextField(30);
        partOfContent = new JTextField(30);
        maxSize = new JTextField(30);
        minSize = new JTextField(30);
        patch = new JTextField(30);
        JLabel partOfContentLabel = new JLabel("Часть контента");
        JLabel partOfNameLabel = new JLabel("Часть имени");
        JLabel maxSizeLabel = new JLabel("Максимальный размер файла");
        JLabel minSizeLabel = new JLabel("Минимальный размер файла");
        JLabel patchLabel = new JLabel("Путь начала поиска");
        JButton search = new JButton("Поиск");
        JButton reset = new JButton("Очистить");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu help = new JMenu("Помощь");
        JMenuItem saveAs = new JMenuItem("Сохранить результат как...");
        JMenuItem exit = new JMenuItem("Выход");
        JMenuItem aboutIt = new JMenuItem("О программе");
        file.add(saveAs);
        file.add(exit);
        help.add(aboutIt);
        bar.add(file);
        bar.add(help);
        search.setBackground(Color.GREEN);
        reset.setBackground(Color.RED);
        JPanel footer = new JPanel();
        JPanel right = new JPanel();
        JPanel center = new JPanel();
        center.add(patchLabel);
        center.add(patch);
        center.add(partOfNameLabel);
        center.add(partOfName);
        center.add(partOfContentLabel);
        center.add(partOfContent);
        center.add(maxSizeLabel);
        center.add(maxSize);
        center.add(minSizeLabel);
        center.add(minSize);
        footer.setLayout(new FlowLayout());
        right.setLayout(new FlowLayout());
        footer.add(search);
        footer.add(reset);
        JScrollPane scroll = new JScrollPane(result);
        right.add(scroll);
        window.getContentPane().add(BorderLayout.NORTH, bar);
        window.getContentPane().add(BorderLayout.SOUTH, footer);
        window.getContentPane().add(BorderLayout.CENTER, center);
        window.getContentPane().add(BorderLayout.EAST, right);
        window.setVisible(true);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameSearch = partOfName.getText();
                String contentSearch = partOfContent.getText();
                String patchOfSearch = patch.getText();
                String maxSearch = maxSize.getText();
                String minSearch = minSize.getText();
                if (!nameSearch.isEmpty()) {
                    searchFileVisitor.setPartOfName(nameSearch);
                }
                if (!contentSearch.isEmpty()) {
                    searchFileVisitor.setPartOfContent(contentSearch);
                }
                if (!maxSearch.isEmpty()) {
                    try {
                        int maximum = Integer.parseInt(maxSearch);
                        searchFileVisitor.setMaxSize(maximum);
                    }
                    catch (Exception ex) {
                        maxSize.setBackground(Color.RED);
                    }
                }
                if (!minSearch.isEmpty()) {
                    try {
                        int minimum = Integer.parseInt(maxSearch);
                        searchFileVisitor.setMinSize(minimum);
                    }
                    catch (Exception ex) {
                        minSize.setBackground(Color.RED);
                    }
                }
                if (!patchOfSearch.isEmpty()) {
                    try {
                        Files.walkFileTree(Paths.get(patchOfSearch), searchFileVisitor);
                        List<Path> foundFiles = searchFileVisitor.getFoundFiles();
                        for (Path file : foundFiles) {
                            result.append(file.normalize().toString() + file.getFileName());
                            result.append("\n");
                        }
                        if (foundFiles.isEmpty()) {
                            result.append("Ничего не найдено");
                        }
                    }
                    catch (IOException ioe) {
                        JOptionPane.showMessageDialog(window, "Ошибка обработки");
                    }
                }
            }
        });
        aboutIt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(window, "Версия поисковика 1.0 alpha");
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                try (FileWriter writer = new FileWriter("C:/temp/result.txt", false))

                {
                    writer.write(result.getText());
                    writer.flush();
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
                */
                JOptionPane.showMessageDialog(window,"Ну допустим сохранили");
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxSize.setText("");
                minSize.setText("");
                partOfContent.setText("");
                partOfName.setText("");
                patch.setText("");
                result.setText("");
                searchFileVisitor = new SearchFileVisitor();
            }
        });
    }
}
