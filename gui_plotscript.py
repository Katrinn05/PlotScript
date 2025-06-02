"""
Krosplatformowa aplikacja PyQt5 dla PlotScript.exe (Windows) lub PlotScript (Linux),
z interfejsem w języku polskim. Pozwala:
  - Otwierać i edytować istniejące pliki .plot,
  - Generować pliki .plot na podstawie podanych parametrów (osi lub kodu C++),
  - Wyświetlać obraz w natywnym rozmiarze,
  - Przybliżać/oddalać wczytany wykres za pomocą przycisków + i -,
  - Bundlowanie pod PyInstaller (ścieżka do obrazów – zawsze dist/generated).
"""

import sys
import os
import re
import tempfile

from PyQt5.QtCore import Qt, QProcess
from PyQt5.QtGui import QPixmap
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QAction, QFileDialog,
    QLabel, QTextEdit, QTabWidget, QVBoxLayout, QHBoxLayout,
    QPushButton, QLineEdit, QTextBrowser, QSizePolicy,
    QMessageBox, QFormLayout, QGroupBox, QRadioButton, QButtonGroup,
    QSplitter
)


def resource_path(relative_path: str) -> str:
    """
    Zwraca absolutną ścieżkę do pliku w bundlowanym folderze PyInstaller
    lub w bieżącym katalogu, jeżeli nie jest to bundling.
    """
    if hasattr(sys, '_MEIPASS'):
        return os.path.join(sys._MEIPASS, relative_path)
    return os.path.join(os.path.abspath(os.path.dirname(__file__)), relative_path)


def find_plot_script_executable() -> str:
    """
    Próbuje znaleźć plik wykonywalny PlotScript:
      - Windows  → PlotScript.exe
      - macOS    → PlotScript (pod innym folderem „MacBuild” lub tam, gdzie upakował PyInstaller)
      - Linux/Unix → PlotScript (np. build/Debug/PlotScript)
    """
    base = os.path.abspath(os.path.dirname(__file__))

    # Windows
    if sys.platform.startswith("win"):
        candidates = [
            resource_path("PlotScript.exe"),
            os.path.join(base, "PlotScript.exe"),
            os.path.join(base, "build", "Debug", "PlotScript.exe"),
            os.path.join(base, "build", "Release", "PlotScript.exe"),
        ]

    # macOS 
    elif sys.platform == "darwin":
        candidates = [
            resource_path("PlotScript"),            
            os.path.join(base, "PlotScript"),       
            os.path.join(base, "MacBuild", "PlotScript"), 
            os.path.join(base, "build", "Debug", "PlotScript"),
            os.path.join(base, "build", "Release", "PlotScript"),
        ]

    # Linux
    else:
        candidates = [
            resource_path("PlotScript"),
            os.path.join(base, "PlotScript"),
            os.path.join(base, "build", "Debug", "PlotScript"),
            os.path.join(base, "build", "Release", "PlotScript"),
        ]

    for path in candidates:
        if os.path.isfile(path) and os.access(path, os.X_OK):
            return path

    return ""



def parse_output_name(plot_text: str) -> str:
    """
    Wyciąga z zawartości .plot nazwaną wartość output: 'plik.png';
    zwraca samą nazwę pliku (np. 'wykres.png') albo pusty string, jeżeli nie znajdzie.
    """
    match = re.search(r"output\s*:\s*['\"]\s*([^'\"]+)\s*['\"]", plot_text, re.IGNORECASE)
    if match:
        return match.group(1).strip()
    return ""


def ensure_generated_folder_exists() -> str:
    """
    Zwraca absolutną ścieżkę do katalogu dist/generated (relatywnie do katalogu, w którym leży ten skrypt).
    Jeśli katalog nie istnieje, zostaje utworzony.
    """
    base = os.path.abspath(os.path.dirname(__file__))
    gen_dir = os.path.join(base, "dist", "generated")
    try:
        os.makedirs(gen_dir, exist_ok=True)
    except Exception:
        return ""
    return gen_dir


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("PlotScript GUI")
        self.resize(1000, 700)

        # ===== Zmienne =====
        self.current_plot_path = ""       # Pełna ścieżka do otwartego pliku .plot
        self.generated_temp_path = None   # Tymczasowy plik .plot (jeżeli generowano)
        self.output_image_path = ""       # Ścieżka do wygenerowanego pliku obrazu
        self.process = None               # QProcess dla PlotScript
        self.current_pixmap = QPixmap()   # Oryginalna pixmapa, aby skalować
        self.scale_factor = 1.0           # Współczynnik skalowania obrazu

        # ===== Menu =====
        open_action = QAction("&Otwórz .plot", self)
        open_action.setShortcut("Ctrl+O")
        open_action.triggered.connect(self.open_plot_file)

        menubar = self.menuBar()
        file_menu = menubar.addMenu("&Plik")
        file_menu.addAction(open_action)

        # ===== Centralny widget – Splitter: po lewej controls, po prawej obraz =====
        central = QWidget()
        main_layout = QHBoxLayout(central)
        splitter = QSplitter(Qt.Horizontal)

        # ------ Lewa część: zakładki i log ------
        left_widget = QWidget()
        left_layout = QVBoxLayout(left_widget)
        left_layout.setContentsMargins(5, 5, 5, 5)
        left_layout.setSpacing(10)

        self.tabs = QTabWidget()
        self.tabs.setTabPosition(QTabWidget.North)

        # ---- Zakładka 1: Edytor pliku .plot ----
        self.tab_file = QWidget()
        file_layout = QVBoxLayout(self.tab_file)
        file_layout.setContentsMargins(0, 0, 0, 0)
        file_layout.setSpacing(5)

        self.editor = QTextEdit()
        self.editor.setPlaceholderText("Tutaj można edytować plik .plot")
        file_layout.addWidget(self.editor)

        btn_run_file = QPushButton("Uruchom")
        btn_run_file.clicked.connect(self.run_plotscript)
        file_layout.addWidget(btn_run_file, alignment=Qt.AlignRight)

        self.tabs.addTab(self.tab_file, "Edytor pliku")

        # ---- Zakładka 2: Generuj z parametrów ----
        self.tab_generate = QWidget()
        gen_layout = QVBoxLayout(self.tab_generate)
        gen_layout.setContentsMargins(0, 0, 0, 0)
        gen_layout.setSpacing(5)

        form_group = QGroupBox("Parametry wykresu")
        form_layout = QFormLayout(form_group)

        # Axis1 (lista wartości)
        self.input_axis1 = QLineEdit()
        self.input_axis1.setPlaceholderText("np. 0,1,2,3,4,5")
        form_layout.addRow("Axis 1:", self.input_axis1)

        # Radio: wartości lub kod C++
        self.rb_values = QRadioButton("Lista wartości (rozdzielone przecinkami)")
        self.rb_values.setChecked(True)
        self.rb_code = QRadioButton("Funkcja C++ (wstaw kod poniżej)")
        self.bg_axis2 = QButtonGroup()
        self.bg_axis2.addButton(self.rb_values)
        self.bg_axis2.addButton(self.rb_code)
        form_layout.addRow(self.rb_values)
        form_layout.addRow(self.rb_code)

        # Axis2 jako wartości
        self.input_axis2_values = QLineEdit()
        self.input_axis2_values.setPlaceholderText("np. 0,1,4,9,16,25")
        form_layout.addRow("Axis 2:", self.input_axis2_values)

        # Axis2 jako kod C++
        self.input_axis2_code = QTextEdit()
        self.input_axis2_code.setPlaceholderText(
            "// Wklej tutaj kod C++:\n"
            "double f(double x) {\n"
            "    return x * x;\n"
            "}"
        )
        self.input_axis2_code.setVisible(False)
        form_layout.addRow(self.input_axis2_code)

        # Przełączanie widoku Axis2
        self.rb_values.toggled.connect(self._toggle_axis2_input)

        # Output (nazwa pliku)
        self.input_output = QLineEdit()
        self.input_output.setPlaceholderText("np. wykres.png lub wykres.bmp")
        form_layout.addRow("Output:", self.input_output)

        gen_layout.addWidget(form_group)

        btn_run_generate = QPushButton("Uruchom")
        btn_run_generate.clicked.connect(self.run_plotscript)
        gen_layout.addWidget(btn_run_generate, alignment=Qt.AlignRight)

        self.tabs.addTab(self.tab_generate, "Generuj z parametrów")

        left_layout.addWidget(self.tabs)

        # ---- Log wykonania ----
        self.log = QTextBrowser()
        self.log.setReadOnly(True)
        self.log.setPlaceholderText("Log wykonania PlotScript...")
        left_layout.addWidget(self.log, stretch=1)

        splitter.addWidget(left_widget)
        splitter.setStretchFactor(0, 0)

        # ------ Prawa część: podgląd obrazu i zoom ------
        right_widget = QWidget()
        right_layout = QVBoxLayout(right_widget)
        right_layout.setContentsMargins(5, 5, 5, 5)
        right_layout.setSpacing(5)

        # Panel zoom: przyciski + i -
        zoom_layout = QHBoxLayout()
        btn_zoom_out = QPushButton("-")
        btn_zoom_out.setFixedSize(30, 30)
        btn_zoom_out.clicked.connect(self.zoom_out)
        btn_zoom_in = QPushButton("+")
        btn_zoom_in.setFixedSize(30, 30)
        btn_zoom_in.clicked.connect(self.zoom_in)
        zoom_layout.addStretch()
        zoom_layout.addWidget(btn_zoom_out)
        zoom_layout.addWidget(btn_zoom_in)
        zoom_layout.addStretch()

        right_layout.addLayout(zoom_layout)

        # QLabel dla obrazu
        self.image_label = QLabel(alignment=Qt.AlignCenter)
        self.image_label.setText("Tutaj wyświetli się wykres")
        self.image_label.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        self.image_label.setStyleSheet(
            "QLabel { background-color : #f0f0f0; border: 1px solid #ccc; }"
        )
        right_layout.addWidget(self.image_label)

        splitter.addWidget(right_widget)
        splitter.setStretchFactor(1, 1)

        main_layout.addWidget(splitter)
        self.setCentralWidget(central)

        # Pasek stanu
        self.statusBar().showMessage("Gotowe")

    def _toggle_axis2_input(self):
        """
        Pokazuje pole do wpisywania wartości axis2 lub kod C++ w zależności od wybranego radiobuttona.
        """
        if self.rb_values.isChecked():
            self.input_axis2_values.setVisible(True)
            self.input_axis2_code.setVisible(False)
        else:
            self.input_axis2_values.setVisible(False)
            self.input_axis2_code.setVisible(True)

    def open_plot_file(self):
        """
        Otwiera dialog wyboru pliku .plot, ładuje zawartość do edytora i zapisuje ścieżkę.
        Próbujemy również załadować istniejący obraz z dist/generated/<nazwa>.
        """
        fname, _ = QFileDialog.getOpenFileName(
            self, "Otwórz plik .plot", "", "Pliki .plot (*.plot);;Wszystkie pliki (*)"
        )
        if not fname:
            return

        try:
            with open(fname, "r", encoding="utf-8") as f:
                text = f.read()
        except Exception as e:
            QMessageBox.critical(self, "Błąd", f"Nie można otworzyć pliku:\n{e}")
            return

        self.current_plot_path = fname
        self.editor.setPlainText(text)
        self.statusBar().showMessage(f"Otworzono: {os.path.basename(fname)}")
        self.log.clear()

        out_name = parse_output_name(text)
        if out_name:
            base = os.path.abspath(os.path.dirname(__file__))
            img_path = os.path.join(base, "dist", "generated", out_name)
            if os.path.isfile(img_path):
                self.output_image_path = img_path
                self._load_image_to_label()
            else:
                self.image_label.setText("Brak wygenerowanego obrazu w dist/generated")
        else:
            self.image_label.setText("Brak pola 'output' w pliku .plot")

    def run_plotscript(self):
        """
        Obsługa przycisku 'Uruchom'. W zależności od wybranej zakładki:
          - 'Edytor pliku': zapisuje zawartość edytora do istniejącego pliku .plot,
             uruchamia PlotScript i potem wczytuje wynik z dist/generated.
          - 'Generuj z parametrów': tworzy tymczasowy .plot, uruchamia PlotScript,
             a wynik szuka w dist/generated.
        Po zakończeniu uruchomienia wyświetla log stdout/stderr i ładuje obraz.
        """
        # Czyść log i resetuj stan obrazu
        self.log.clear()
        self.image_label.clear()
        self.output_image_path = ""
        self.current_pixmap = QPixmap()
        self.scale_factor = 1.0

        exe_path = find_plot_script_executable()
        if not exe_path:
            QMessageBox.critical(self, "Błąd", "Nie znaleziono PlotScript.exe/PlotScript.")
            self.statusBar().showMessage("Brak PlotScript")
            return

        # Przygotowanie argumentów:
        if self.tabs.currentWidget() == self.tab_file:
            # --- Edytor pliku ---
            if not self.current_plot_path:
                QMessageBox.warning(self, "Uwaga", "Najpierw otwórz plik .plot.")
                self.statusBar().showMessage("Brak pliku .plot")
                return
            # Zapisz zawartość edytora do istniejącego .plot
            try:
                with open(self.current_plot_path, "w", encoding="utf-8") as f:
                    f.write(self.editor.toPlainText())
            except Exception as e:
                QMessageBox.critical(self, "Błąd", f"Nie można zapisać pliku:\n{e}")
                self.statusBar().showMessage("Błąd zapisu")
                return

            plot_path_to_run = self.current_plot_path
            out_name = parse_output_name(self.editor.toPlainText())
            if out_name:
                base = os.path.abspath(os.path.dirname(__file__))
                self.output_image_path = os.path.join(base, "dist", "generated", out_name)
            else:
                self.output_image_path = ""
        else:
            axis1_text = self.input_axis1.text().strip()
            if not axis1_text:
                QMessageBox.warning(self, "Uwaga", "Podaj wartości dla Axis 1.")
                self.statusBar().showMessage("Brak Axis1")
                return
            axis1_list = [s.strip() for s in axis1_text.split(",") if s.strip()]
            if not axis1_list:
                QMessageBox.warning(self, "Uwaga", "Niepoprawne wartości w Axis 1.")
                self.statusBar().showMessage("Niepoprawne Axis1")
                return

            if self.rb_values.isChecked():
                axis2_text = self.input_axis2_values.text().strip()
                if not axis2_text:
                    QMessageBox.warning(self, "Uwaga", "Podaj wartości dla Axis 2.")
                    self.statusBar().showMessage("Brak Axis2")
                    return
                axis2_list = [s.strip() for s in axis2_text.split(",") if s.strip()]
                if not axis2_list:
                    QMessageBox.warning(self, "Uwaga", "Niepoprawne wartości w Axis 2.")
                    self.statusBar().showMessage("Niepoprawne Axis2")
                    return
                axis2_block = "[{}]".format(", ".join(axis2_list))
            else:
                code = self.input_axis2_code.toPlainText().strip()
                if not code:
                    QMessageBox.warning(self, "Uwaga", "Podaj kod C++ dla Axis 2.")
                    self.statusBar().showMessage("Brak kodu C++")
                    return
                axis2_block = "func(\n    $CPP$\n" + code + "\n    $$\n)"

            out_name = self.input_output.text().strip()
            if not out_name:
                QMessageBox.warning(self, "Uwaga", "Podaj nazwę pliku wyjściowego (np. wykres.png).")
                self.statusBar().showMessage("Brak output")
                return

            # Składamy treść pliku .plot
            plot_content = "myPlot {\n"
            plot_content += "  axis1: [{}];\n".format(", ".join(axis1_list))
            plot_content += "  axis2: {};\n".format(axis2_block)
            plot_content += "  output: '{}';\n".format(out_name)
            plot_content += "}\n"

            # Zapisujemy do pliku tymczasowego
            try:
                temp = tempfile.NamedTemporaryFile(mode='w', suffix=".plot", delete=False, encoding="utf-8")
                temp.write(plot_content)
                temp.close()
                self.generated_temp_path = temp.name
                plot_path_to_run = self.generated_temp_path
                base = os.path.abspath(os.path.dirname(__file__))
                self.output_image_path = os.path.join(base, "dist", "generated", out_name)
            except Exception as e:
                QMessageBox.critical(self, "Błąd", f"Nie można utworzyć pliku tymczasowego:\n{e}")
                self.statusBar().showMessage("Błąd pliku tymczasowego")
                return

        # ===== Tworzymy folder dist/generated i ustawiamy go jako working directory =====
        gen_folder = ensure_generated_folder_exists()
        if not gen_folder:
            QMessageBox.critical(self, "Błąd", "Nie można utworzyć katalogu dist/generated.")
            self.statusBar().showMessage("Brak katalogu dist/generated")
            return

        self.statusBar().showMessage("Trwa wykonywanie PlotScript...")
        if self.process:
            self.process.kill()
            self.process = None

        self.process = QProcess(self)
        self.process.setWorkingDirectory(gen_folder)
        self.process.readyReadStandardOutput.connect(self._read_stdout)
        self.process.readyReadStandardError.connect(self._read_stderr)
        self.process.finished.connect(self._process_finished)

        self.process.start(exe_path, [plot_path_to_run])
        if not self.process.waitForStarted():
            QMessageBox.critical(self, "Błąd", "Nie udało się uruchomić PlotScript.")
            self.statusBar().showMessage("Błąd uruchamiania")
            return

    def _read_stdout(self):
        """
        Odczyt standardowego wyjścia i dopisanie do loga.
        """
        if not self.process:
            return
        data = self.process.readAllStandardOutput().data().decode(errors='ignore')
        self.log.append(data)

    def _read_stderr(self):
        """
        Odczyt błędnego wyjścia i dopisanie do loga na czerwono.
        """
        if not self.process:
            return
        data = self.process.readAllStandardError().data().decode(errors='ignore')
        if data:
            self.log.append(f"<span style=\"color:red;\">{data}</span>")

    def _process_finished(self, exitCode: int, exitStatus):
        """
        Po zakończeniu PlotScript: jeśli wygenerowano obraz, wczytaj i pokaż;
        w przeciwnym razie wypisz komunikat o błędzie.
        """
        if exitCode == 0:
            if self.output_image_path and os.path.isfile(self.output_image_path):
                self._load_image_to_label()
                self.statusBar().showMessage("Ukończono pomyślnie")
            else:
                self.log.append(f"<span style=\"color:red;\"><b>Nie znaleziono obrazu: {self.output_image_path}</b></span>")
                self.statusBar().showMessage("Brak obrazu")
        else:
            self.statusBar().showMessage(f"Zakończono z błędem (kod {exitCode})")

    def _load_image_to_label(self):
        """
        Ładuje plik obrazu do pixmapy i wyświetla w QLabel (ustawia current_pixmap
        i resetuje scale_factor, a następnie wywołuje update_image_display).
        """
        pix = QPixmap(self.output_image_path)
        if pix.isNull():
            self.log.append(f"<span style=\"color:red;\"><b>Nie można załadować obrazu {self.output_image_path}</b></span>")
            return

        # Zapisujemy oryginalną pixmapę i resetujemy scale_factor
        self.current_pixmap = pix
        self.scale_factor = 1.0
        self.update_image_display()

    def update_image_display(self):
       
        if self.current_pixmap.isNull():
            return
        # Oblicz nowy rozmiar
        orig_size = self.current_pixmap.size()
        new_width = int(orig_size.width() * self.scale_factor)
        new_height = int(orig_size.height() * self.scale_factor)
        # Skalowanie z zachowaniem proporcji
        scaled = self.current_pixmap.scaled(new_width, new_height, Qt.KeepAspectRatio, Qt.SmoothTransformation)
        self.image_label.setPixmap(scaled)

    def zoom_in(self):
        
        if self.current_pixmap.isNull():
            return
        self.scale_factor *= 1.25
        self.update_image_display()

    def zoom_out(self):
       
        if self.current_pixmap.isNull():
            return
        self.scale_factor /= 1.25
        if self.scale_factor < 0.1:
            self.scale_factor = 0.1
        self.update_image_display()


def main():
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())


if __name__ == "__main__":
    main()
